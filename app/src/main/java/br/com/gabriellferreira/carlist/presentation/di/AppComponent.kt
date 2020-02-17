package br.com.gabriellferreira.carlist.presentation.di

import br.com.gabriellferreira.carlist.presentation.di.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [AppModule::class, RepositoryModule::class, ApiModule::class])
interface AppComponent {

    fun newControllerComponent(controllerModule: ControllerModule): ControllerComponent
}