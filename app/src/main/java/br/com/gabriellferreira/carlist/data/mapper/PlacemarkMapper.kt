@file:Suppress("MemberVisibilityCanBePrivate")

package br.com.gabriellferreira.carlist.data.mapper

import android.content.res.Resources
import br.com.gabriellferreira.carlist.R
import br.com.gabriellferreira.carlist.data.model.PlacemarkData
import br.com.gabriellferreira.carlist.data.model.PlacemarkResponseData
import br.com.gabriellferreira.carlist.domain.model.Coordinates
import br.com.gabriellferreira.carlist.domain.model.Placemark
import javax.inject.Inject

class PlacemarkMapper @Inject constructor(
    private val resources: Resources
) {

    fun map(data: PlacemarkResponseData): List<Placemark> =
        data.placemarks.map {
            map(it)
        }

    fun map(data: PlacemarkData) = Placemark(
        engineType = data.engineType ?: "",
        coordinates = getCoordinates(data.coordinates?.get(1), data.coordinates?.get(0)),
        address = data.address ?: "",
        fuelString = resources.getString(R.string.fuel_level_description, data.fuel ?: 0),
        name = data.name ?: ""
    )

    private fun getCoordinates(lat: Double?, long: Double?) =
        Coordinates(
            latitude = lat ?: 0.0,
            longitude = long ?: 0.0
        )
}