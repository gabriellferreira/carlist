package br.com.gabriellferreira.carlist.data.repository

import br.com.gabriellferreira.carlist.data.mapper.PlacemarkMapper
import br.com.gabriellferreira.carlist.data.network.api.PlacemarkApi
import br.com.gabriellferreira.carlist.domain.model.Placemark
import br.com.gabriellferreira.carlist.domain.repository.PlacemarkRepository
import io.reactivex.Single
import javax.inject.Inject

class PlacemarkDataRepository @Inject constructor(
    private val placemarkApi: PlacemarkApi,
    private val mapper: PlacemarkMapper
) : PlacemarkRepository {

    override fun fetchPlacemarkList(): Single<List<Placemark>> =
        placemarkApi.fetchPlacemarkList()
            .map {
                mapper.map(it)
            }
}