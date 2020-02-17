package br.com.gabriellferreira.carlist.data.network.api

import br.com.gabriellferreira.carlist.data.model.PlacemarkResponseData
import br.com.gabriellferreira.carlist.data.network.service.PlacemarkService
import io.reactivex.Single
import javax.inject.Inject

class PlacemarkApi @Inject constructor(
    private val service: PlacemarkService
) {

    @Suppress("UNUSED_PARAMETER")
    fun fetchPlacemarkList(): Single<PlacemarkResponseData> =
        service.fetchPlacemarkList()
}