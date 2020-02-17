package br.com.gabriellferreira.carlist.presentation.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.gabriellferreira.carlist.R
import br.com.gabriellferreira.carlist.domain.model.NetworkState
import br.com.gabriellferreira.carlist.domain.model.NetworkState.State.*
import br.com.gabriellferreira.carlist.domain.model.Placemark
import br.com.gabriellferreira.carlist.domain.model.Retryable
import br.com.gabriellferreira.carlist.presentation.di.AppApplication
import br.com.gabriellferreira.carlist.presentation.di.ControllerModule
import br.com.gabriellferreira.carlist.presentation.util.extension.hide
import br.com.gabriellferreira.carlist.presentation.util.extension.show
import br.com.gabriellferreira.carlist.presentation.util.extension.showRetrySnackbar
import br.com.gabriellferreira.carlist.presentation.view.adapter.PlacemarkListAdapter
import br.com.gabriellferreira.carlist.presentation.view.viewmodel.PlacemarkListViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_placemark_list.*
import javax.inject.Inject

class PlacemarkListActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var viewModel: PlacemarkListViewModel

    private val adapter by lazy { PlacemarkListAdapter() }

    private val mControllerComponent by lazy {
        (application as AppApplication).getApplicationComponent()
            .newControllerComponent(ControllerModule(this))
    }
    private var clicksDisposable: Disposable? = null
    private lateinit var mMap: GoogleMap
    private val clusterManager by lazy { ClusterManager<Placemark>(this, mMap) }

    companion object {
        const val HAMBURG_LATITUDE = 53.5586941
        const val HAMBURG_LONGITUDE = 9.78774
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placemark_list)
        mControllerComponent.inject(this)
        setupRecycler()
        initObservers()

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.placemark_list_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onDestroy() {
        clicksDisposable?.dispose()
        super.onDestroy()
    }

    private fun initObservers() {
        viewModel.itemList.observe(this,
            Observer<List<Placemark>> { items ->
                adapter.submitList(items)
                clusterManager.addItems(items)
                centerCameraHamburg()
            })
        viewModel.networkState.observe(this,
            Observer<NetworkState> {
                placemark_list_error?.setOnClickListener(null)
                when (it.state) {
                    LOADED -> {
                        placemark_list_map?.show()
                        placemark_list_error?.hide()
                        placemark_list_progress?.hide()
                    }
                    IN_PROGRESS -> {
                        if (adapter.itemCount == 0) {
                            placemark_list_map?.show()
                            placemark_list_error?.hide()
                            placemark_list_progress?.hide()
                        }
                    }
                    ERROR -> {
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

    private fun centerCameraHamburg() {
        val hamburg = LatLng(HAMBURG_LATITUDE, HAMBURG_LONGITUDE)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hamburg, 8f))
    }

    private fun showRetrySnackbar(retryable: Retryable?) {
        retryable?.let {
            showRetrySnackbar(placemark_list_parent, getString(R.string.generic_retry), retryable)
        }
    }

    private fun setupRecycler() {
        placemark_list_recycler?.layoutManager = LinearLayoutManager(this)
        placemark_list_recycler?.adapter = adapter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnCameraIdleListener(clusterManager)
        mMap.setOnMarkerClickListener(clusterManager)
    }
}
