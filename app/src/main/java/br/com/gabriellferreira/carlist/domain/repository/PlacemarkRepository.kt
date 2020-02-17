package br.com.gabriellferreira.carlist.domain.repository

import br.com.gabriellferreira.carlist.domain.model.Placemark
import io.reactivex.Single

interface PlacemarkRepository {
    fun fetchPlacemarkList(): Single<List<Placemark>>
}