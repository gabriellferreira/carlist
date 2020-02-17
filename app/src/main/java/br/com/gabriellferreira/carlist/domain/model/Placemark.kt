package br.com.gabriellferreira.carlist.domain.model

import androidx.recyclerview.widget.DiffUtil

class Placemark(
    val engineType: String,
    val coordinates: Triple<Double, Double, Double>
)

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