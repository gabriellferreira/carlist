package br.com.gabriellferreira.carlist.presentation.di

import br.com.gabriellferreira.carlist.data.repository.PlacemarkDataRepository
import br.com.gabriellferreira.carlist.domain.repository.PlacemarkRepository
import br.com.gabriellferreira.carlist.presentation.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
open class RepositoryModule {

    @Provides
    @ApplicationScope
    open fun providePlacemarkRepository(repository: PlacemarkDataRepository): PlacemarkRepository =
        repository
}