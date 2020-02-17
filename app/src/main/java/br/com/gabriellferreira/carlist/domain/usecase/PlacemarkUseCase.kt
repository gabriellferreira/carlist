package br.com.gabriellferreira.carlist.domain.usecase

import br.com.gabriellferreira.carlist.domain.model.Placemark
import br.com.gabriellferreira.carlist.domain.repository.PlacemarkRepository
import io.reactivex.Single
import javax.inject.Inject

class PlacemarkUseCase @Inject constructor(
    private val repository: PlacemarkRepository
) {

    fun fetchPlacemarkList(): Single<List<Placemark>> =
        repository.fetchPlacemarkList()
}