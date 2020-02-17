package br.com.gabriellferreira.carlist.presentation.di

import android.content.Context
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Scheduler

class TestAppModule(appApplication: AppApplication) : AppModule(appApplication) {

    override fun provideApplicationContext(): Context = mock()

    override fun provideScheduler(): Scheduler = mock()
}