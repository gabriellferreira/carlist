package br.com.gabriellferreira.carlist.domain.model

import androidx.recyclerview.widget.DiffUtil
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class Placemark(
    val engineType: String,
    val address: String,
    val coordinates: Coordinates,
    val fuelString: String,
    val name: String
) : ClusterItem {

    override fun getPosition() = LatLng(coordinates.latitude, coordinates.longitude)

    override fun getTitle(): String = name

    override fun getSnippet(): String = address
}

class Coordinates(
    val latitude: Double,
    val longitude: Double
)

class PlacemarkDiffUtil : DiffUtil.ItemCallback<Placemark>() {

    override fun areItemsTheSame(oldItem: Placemark, newItem: Placemark): Boolean {
        return oldItem.coordinates.latitude == newItem.coordinates.latitude &&
                oldItem.coordinates.longitude == newItem.coordinates.longitude
    }

    override fun areContentsTheSame(oldItem: Placemark, newItem: Placemark): Boolean {
        return oldItem.engineType == newItem.engineType
    }
}