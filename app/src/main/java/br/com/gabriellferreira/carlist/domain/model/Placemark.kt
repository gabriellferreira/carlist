package br.com.gabriellferreira.carlist.domain.model

import androidx.recyclerview.widget.DiffUtil
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class Placemark(
    val engineType: String,
    val coordinates: Triple<Double, Double, Double>,
    val address: String,
    val fuel: String,
    val name: String
) : ClusterItem {

    override fun getPosition() = LatLng(coordinates.second, coordinates.first)

    override fun getTitle(): String = name

    override fun getSnippet(): String = name
}

class PlacemarkDiffUtil : DiffUtil.ItemCallback<Placemark>() {

    override fun areItemsTheSame(oldItem: Placemark, newItem: Placemark): Boolean {
        return oldItem.coordinates.first == newItem.coordinates.first &&
                oldItem.coordinates.second == newItem.coordinates.second &&
                oldItem.coordinates.third == newItem.coordinates.third
    }

    override fun areContentsTheSame(oldItem: Placemark, newItem: Placemark): Boolean {
        return oldItem.engineType == newItem.engineType
    }
}