package br.com.gabriellferreira.carlist.presentation.view.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import br.com.gabriellferreira.carlist.R
import br.com.gabriellferreira.carlist.domain.model.NetworkState
import br.com.gabriellferreira.carlist.domain.model.Placemark
import br.com.gabriellferreira.carlist.presentation.di.AppApplication
import br.com.gabriellferreira.carlist.presentation.di.ControllerModule
import br.com.gabriellferreira.carlist.presentation.util.extension.hide
import br.com.gabriellferreira.carlist.presentation.util.extension.showRetrySnackbar
import br.com.gabriellferreira.carlist.presentation.view.adapter.PlacemarkListAdapter
import br.com.gabriellferreira.carlist.presentation.view.viewmodel.PlacemarkListViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_placemark_list.*
import javax.inject.Inject

class PlacemarkListActivity : BaseMapActivity() {

    @Inject
    lateinit var viewModel: PlacemarkListViewModel

    private val adapter by lazy { PlacemarkListAdapter() }

    private val mControllerComponent by lazy {
        (application as AppApplication).getApplicationComponent()
            .newControllerComponent(ControllerModule(this))
    }
    private var clicksDisposable: Disposable? = null
    private var isItemListHidden = false
    override val defaultZoomLevel by lazy { viewModel.getDefaultZoomLevel() }
    override val defaultLocation by lazy { viewModel.getDefaultLocation() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placemark_list)
        mControllerComponent.inject(this)
        setupRecycler()
        initObservers()
        initMap()
    }

    override fun initMap() {
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
                        placemark_list_progress?.hide()

                        adapter.submitList(it.result)
                        clusterManager.addItems(it.result)
                        clusterManager.cluster()
                    }
                    is NetworkState.InProgress -> {
                        placemark_list_progress?.hide()
                    }
                    is NetworkState.Error -> {
                        placemark_list_progress?.hide()
                        showRetrySnackbar(it)
                    }
                }
            })
    }

    private fun showRetrySnackbar(error: NetworkState.Error) {
        error.retryable?.let {
            showRetrySnackbar(placemark_list_parent, getString(R.string.generic_retry), it)
        }
    }

    private fun setupRecycler() {
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
                animateMapToPosition(placemark.coordinates)
                closeDrawers()
            }
            .subscribe()
    }

    private fun closeDrawers() {
        placemark_view?.closeDrawers()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        super.onMapReady(googleMap)
        map?.setOnMapClickListener {
            if (isItemListHidden) {
                clusterManager.markerCollection.showAll()
                isItemListHidden = !isItemListHidden
            }
        }
    }

    override fun onPlacemarkListItemClick(placemark: Placemark) {
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
}