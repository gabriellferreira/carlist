package br.com.gabriellferreira.carlist.domain.model

import androidx.recyclerview.widget.DiffUtil
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class Placemark(
    val engineType: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val fuel: String,
    val name: String
) : ClusterItem {

    override fun getPosition() = LatLng(latitude, longitude)

    override fun getTitle(): String = name

    override fun getSnippet(): String = address
}

class PlacemarkDiffUtil : DiffUtil.ItemCallback<Placemark>() {

    override fun areItemsTheSame(oldItem: Placemark, newItem: Placemark): Boolean {
        return oldItem.latitude == newItem.latitude &&
                oldItem.longitude == newItem.longitude
    }

    override fun areContentsTheSame(oldItem: Placemark, newItem: Placemark): Boolean {
        return oldItem.engineType == newItem.engineType
    }
}