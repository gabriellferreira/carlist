package br.com.gabriellferreira.carlist.presentation.di

import br.com.gabriellferreira.carlist.data.network.api.base.MTribesApi
import br.com.gabriellferreira.carlist.data.network.service.PlacemarkService
import br.com.gabriellferreira.carlist.presentation.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ApiModule {

    @Provides
    @ApplicationScope
    fun providePlacemarkService(api: MTribesApi): PlacemarkService {
        return api.build().create(PlacemarkService::class.java)
    }
}