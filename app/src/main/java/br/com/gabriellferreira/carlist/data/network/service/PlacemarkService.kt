package br.com.gabriellferreira.carlist.data.network.service

import br.com.gabriellferreira.carlist.data.model.PlacemarkResponseData
import io.reactivex.Single
import retrofit2.http.GET

interface PlacemarkService {

    @GET("locations.json")
    fun fetchPlacemarkList(): Single<PlacemarkResponseData>
}