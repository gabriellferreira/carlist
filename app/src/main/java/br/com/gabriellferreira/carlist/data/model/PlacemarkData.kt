package br.com.gabriellferreira.carlist.data.model

class PlacemarkResponseData(
    val placemarks: List<PlacemarkData>
)

class PlacemarkData(
    val address: String? = null,
    val coordinates: List<Double>? = null,
    val engineType: String? = null,
    val fuel: Int? = null,
    val name: String? = null
)