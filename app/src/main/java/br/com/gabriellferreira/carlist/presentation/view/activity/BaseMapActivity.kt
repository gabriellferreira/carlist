package br.com.gabriellferreira.carlist.presentation.view.activity

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import br.com.gabriellferreira.carlist.domain.model.Coordinates
import br.com.gabriellferreira.carlist.domain.model.Placemark
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@SuppressLint("Registered")
@RuntimePermissions
abstract class BaseMapActivity : AppCompatActivity(), OnMapReadyCallback{

    internal var map: GoogleMap? = null
    internal val clusterManager by lazy { ClusterManager<Placemark>(this, map) }
    abstract val defaultZoomLevel : Float
    abstract val defaultLocation : Coordinates

    abstract fun initMap()

    override fun onMapReady(googleMap: GoogleMap) {
        if (map != null) return
        map = googleMap
        map?.setOnCameraIdleListener(clusterManager)
        map?.setOnMarkerClickListener(clusterManager)
        clusterManager.setOnClusterItemClickListener { placemark ->
            onPlacemarkListItemClick(placemark)
            false
        }
        showUserPositionWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun showUserPosition() {
        map?.isMyLocationEnabled = true
        map?.setOnMyLocationButtonClickListener {
            animateToUserPosition()
            onMyLocationButtonClickListener()
            true
        }
        animateToUserPosition()
    }

    abstract fun onMyLocationButtonClickListener()

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    internal fun centerCameraDefaultLocation(){
        animateMapToPosition(defaultLocation, defaultZoomLevel)
    }

    private fun animateToUserPosition() {
        val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationProviderClient.lastLocation.addOnCompleteListener {
            it.result?.let { location ->
                animateMapToPosition(location.latitude, location.longitude)
            }
        }
    }

    internal fun animateMapToPosition(coordinates: Coordinates, zoomLevel: Float = defaultZoomLevel) {
        animateMapToPosition(coordinates.latitude, coordinates.longitude, zoomLevel)
    }

    private fun animateMapToPosition(lat: Double, long: Double, zoomLevel: Float = defaultZoomLevel) {
        val latLng = LatLng(lat, long)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
    }

    abstract fun onPlacemarkListItemClick(placemark: Placemark)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}