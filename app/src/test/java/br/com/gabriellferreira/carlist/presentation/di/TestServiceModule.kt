package br.com.gabriellferreira.carlist.presentation.di

import br.com.gabriellferreira.carlist.data.network.service.FakePlacemarkService
import br.com.gabriellferreira.carlist.data.network.service.PlacemarkService
import dagger.Module
import dagger.Provides

@Module
class TestServiceModule {

    @Provides
    fun providePlacemarkService(): PlacemarkService = FakePlacemarkService()
}