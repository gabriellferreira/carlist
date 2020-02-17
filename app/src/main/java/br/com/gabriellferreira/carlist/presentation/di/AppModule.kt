package br.com.gabriellferreira.carlist.presentation.di

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

@Module
open class AppModule(private val appApplication: AppApplication) {

    @Provides
    open fun provideApplicationContext(): Context = appApplication

    @Provides
    open fun provideResources(context: Context): Resources = context.resources

    @Provides
    open fun provideScheduler(): Scheduler = Schedulers.io()
}