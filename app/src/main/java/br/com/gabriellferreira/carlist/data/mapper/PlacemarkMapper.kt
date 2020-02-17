package br.com.gabriellferreira.carlist.data.mapper

import br.com.gabriellferreira.carlist.data.model.PlacemarkData
import br.com.gabriellferreira.carlist.data.model.PlacemarkResponseData
import br.com.gabriellferreira.carlist.domain.model.Placemark
import javax.inject.Inject

class PlacemarkMapper @Inject constructor() {

    fun map(data: PlacemarkResponseData): List<Placemark> =
        data.placemarks.map {
            map(it)
        }

    fun map(data: PlacemarkData) = Placemark(
        engineType = data.engineType ?: "",
        coordinates = Triple(
            data.coordinates?.get(0) ?: 0.0,
            data.coordinates?.get(1) ?: 0.0,
            data.coordinates?.get(2) ?: 0.0
        )
    )
}
