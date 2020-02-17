package br.com.gabriellferreira.carlist.presentation.di

import br.com.gabriellferreira.carlist.presentation.di.scope.ControllerScope
import br.com.gabriellferreira.carlist.presentation.view.activity.PlacemarkListActivity
import dagger.Subcomponent

@ControllerScope
@Subcomponent(modules = [ControllerModule::class, ViewModelModule::class])
interface ControllerComponent {

    // View
    fun inject(placemarkListActivity: PlacemarkListActivity)
}