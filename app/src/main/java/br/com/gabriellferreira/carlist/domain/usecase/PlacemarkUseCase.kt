package br.com.gabriellferreira.carlist.domain.usecase

import br.com.gabriellferreira.carlist.domain.model.Coordinates
import br.com.gabriellferreira.carlist.domain.model.Placemark
import br.com.gabriellferreira.carlist.domain.repository.PlacemarkRepository
import io.reactivex.Single
import javax.inject.Inject

class PlacemarkUseCase @Inject constructor(
    private val repository: PlacemarkRepository
) {

    companion object {
        private const val DEFAULT_ZOOM_LEVEL: Float = 15f
        private const val HAMBURG_LATITUDE = 53.552581
        private const val HAMBURG_LONGITUDE = 10.0027537
    }

    fun fetchPlacemarkList(): Single<List<Placemark>> =
        repository.fetchPlacemarkList()

    fun getDefaultLocation(): Coordinates = Coordinates(HAMBURG_LATITUDE, HAMBURG_LONGITUDE)

    fun getDefaultZoomLevel() = DEFAULT_ZOOM_LEVEL
}