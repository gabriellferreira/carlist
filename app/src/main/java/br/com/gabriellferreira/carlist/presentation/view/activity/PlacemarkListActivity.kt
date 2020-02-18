package br.com.gabriellferreira.carlist.presentation.view.activity

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.gabriellferreira.carlist.R
import br.com.gabriellferreira.carlist.domain.model.NetworkState
import br.com.gabriellferreira.carlist.domain.model.Placemark
import br.com.gabriellferreira.carlist.domain.model.Retryable
import br.com.gabriellferreira.carlist.presentation.di.AppApplication
import br.com.gabriellferreira.carlist.presentation.di.ControllerModule
import br.com.gabriellferreira.carlist.presentation.util.extension.hide
import br.com.gabriellferreira.carlist.presentation.util.extension.show
import br.com.gabriellferreira.carlist.presentation.util.extension.showRetrySnackbar
import br.com.gabriellferreira.carlist.presentation.view.adapter.PlacemarkListAdapter
import br.com.gabriellferreira.carlist.presentation.view.viewmodel.PlacemarkListViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_placemark_list.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import javax.inject.Inject


@RuntimePermissions
class PlacemarkListActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var viewModel: PlacemarkListViewModel

    private val adapter by lazy { PlacemarkListAdapter() }

    private val mControllerComponent by lazy {
        (application as AppApplication).getApplicationComponent()
            .newControllerComponent(ControllerModule(this))
    }
    private var clicksDisposable: Disposable? = null
    private var mMap: GoogleMap? = null
    private val clusterManager by lazy { ClusterManager<Placemark>(this, mMap) }
    private var isItemListHidden = false

    companion object {
        const val HAMBURG_LATITUDE = 53.552581
        const val HAMBURG_LONGITUDE = 10.0027537
        const val DEFAULT_ZOOM_LEVEL: Float = 15f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placemark_list)
        mControllerComponent.inject(this)
        setupRecycler()
        initObservers()
        initMapFragment()
    }

    private fun initMapFragment() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.placemark_list_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroy() {
        clicksDisposable?.dispose()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_placemark_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_placemark_list -> {
                placemark_view?.openDrawer(GravityCompat.END)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initObservers() {
        viewModel.itemList.observe(this,
            Observer<NetworkState<List<Placemark>>> {
                placemark_list_error?.setOnClickListener(null)
                when (it) {
                    is NetworkState.Loaded<List<Placemark>> -> {
                        placemark_list_map?.show()
                        placemark_list_error?.hide()
                        placemark_list_progress?.hide()

                        adapter.submitList(it.result)
                        clusterManager.addItems(it.result)
                        clusterManager.cluster()
                    }
                    is NetworkState.InProgress -> {
                        if (adapter.itemCount == 0) {
                            placemark_list_map?.show()
                            placemark_list_error?.hide()
                            placemark_list_progress?.hide()
                        }
                    }
                    is NetworkState.Error -> {
                        if (adapter.itemCount > 0) {
                            placemark_list_progress?.hide()
                            placemark_list_error?.hide()
                            showRetrySnackbar(it.retryable)
                        } else {
                            placemark_list_map?.hide()
                            placemark_list_progress?.hide()
                            placemark_list_error?.show()
                            showRetrySnackbar(it.retryable)
                        }
                    }
                }
            })
    }

    private fun showRetrySnackbar(retryable: Retryable?) {
        retryable?.let {
            showRetrySnackbar(placemark_list_parent, getString(R.string.generic_retry), retryable)
        }
    }

    private fun setupRecycler() {
        placemark_list_recycler?.layoutManager = LinearLayoutManager(this)
        placemark_list_recycler?.adapter = adapter
        clicksDisposable = adapter.onItemClickSubject
            .doOnError { e ->
                Log.e("PlacemarkListActivity", "setupRecycler", e)
            }
            .doOnNext { placemark ->
                clusterManager.markerCollection.showAll()
                isItemListHidden = false
                val marker = clusterManager.markerCollection.markers.firstOrNull {
                    it.title == placemark.name
                }
                onPlacemarkListItemClick(placemark)
                marker?.showInfoWindow()
                animateToPosition(placemark.latitude, placemark.longitude)
                placemark_view?.closeDrawers()
            }
            .subscribe()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (mMap != null) return
        mMap = googleMap
        mMap?.setOnCameraIdleListener(clusterManager)
        mMap?.setOnMarkerClickListener(clusterManager)
        clusterManager.setOnClusterItemClickListener { placemark ->
            onPlacemarkListItemClick(placemark)
            false
        }
        mMap?.setOnMapClickListener {
            if (isItemListHidden) {
                clusterManager.markerCollection.showAll()
                isItemListHidden = !isItemListHidden
            }
        }
        showUserPositionWithPermissionCheck()
    }

    private fun onPlacemarkListItemClick(placemark: Placemark) {
        if (isItemListHidden) {
            clusterManager.markerCollection.showAll()
        } else {
            clusterManager.markerCollection.hideAll()
            clusterManager.markerCollection.markers.firstOrNull {
                it.title == placemark.title
            }?.isVisible = true
        }
        isItemListHidden = !isItemListHidden
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showUserPosition() {
        mMap?.isMyLocationEnabled = true
        mMap?.setOnMyLocationButtonClickListener {
            animateToUserPosition()
            true
        }
        animateToUserPosition()
    }

    private fun animateToUserPosition() {
        val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationProviderClient.lastLocation.addOnCompleteListener {
            it.result?.let { location ->
                animateToPosition(location.latitude, location.longitude)
            }
        }
    }

    private fun animateToPosition(lat: Double, long: Double): Unit? {
        val latLng = LatLng(lat, long)
        return mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL))
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun centerCameraHamburg() {
        val hamburg = LatLng(HAMBURG_LATITUDE, HAMBURG_LONGITUDE)
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(hamburg, DEFAULT_ZOOM_LEVEL))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}