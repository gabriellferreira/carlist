package br.com.gabriellferreira.carlist.presentation.di

import android.content.Context
import android.content.res.Resources
import br.com.gabriellferreira.carlist.presentation.util.RxSchedulersOverrideRule
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Scheduler

class TestAppModule(appApplication: AppApplication) : AppModule(appApplication) {

    override fun provideApplicationContext(): Context = mock()

    override fun provideResources(context: Context): Resources = mock()

    override fun provideScheduler(): Scheduler = RxSchedulersOverrideRule().scheduler
}