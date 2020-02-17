package br.com.gabriellferreira.carlist.presentation.di

import br.com.gabriellferreira.carlist.data.repository.PlacemarkDataRepository
import br.com.gabriellferreira.carlist.domain.repository.PlacemarkRepository
import com.nhaarman.mockitokotlin2.mock

class TestRepositoryModule : RepositoryModule() {

    override fun providePlacemarkRepository(repository: PlacemarkDataRepository): PlacemarkRepository =
        mock()
}