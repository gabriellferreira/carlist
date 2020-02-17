package br.com.gabriellferreira.carlist.presentation.di

import br.com.gabriellferreira.carlist.domain.usecase.PlacemarkUseCaseTest
import br.com.gabriellferreira.carlist.presentation.di.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [AppModule::class, RepositoryModule::class, TestServiceModule::class])
interface TestAppComponent : AppComponent {
    fun inject(placemarkListViewModelTest: PlacemarkUseCaseTest)
}