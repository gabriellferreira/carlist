package br.com.gabriellferreira.carlist.presentation.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.gabriellferreira.carlist.domain.model.Coordinates
import br.com.gabriellferreira.carlist.domain.model.Placemark
import br.com.gabriellferreira.carlist.domain.usecase.PlacemarkUseCase
import br.com.gabriellferreira.carlist.presentation.di.*
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import javax.inject.Inject

class PlacemarkListViewModelTest{

    @Rule
    @JvmField
    var instantTaskRule: InstantTaskExecutorRule? = InstantTaskExecutorRule()

    @Mock
    lateinit var useCase: PlacemarkUseCase

    @Inject
    lateinit var scheduler: Scheduler

    lateinit var viewModel: PlacemarkListViewModel

    @Before
    fun setup(){

        MockitoAnnotations.initMocks(this)
        val component = DaggerTestAppComponent.builder()
            .appModule(TestAppModule(AppApplication()))
            .repositoryModule(TestRepositoryModule())
            .testServiceModule(TestServiceModule())
            .build()
        component.inject(this)

        whenever(useCase.fetchPlacemarkList())
            .thenReturn(
                Single.just(
                    listOf(
                        Placemark(
                            engineType = "eletric",
                            coordinates = Coordinates(0.0, 0.0),
                            address = "Kieler Strasse",
                            fuelString = "Fuel: 45%",
                            name = "EIC-6727"
                        )
                    )
                )
            )

        viewModel = PlacemarkListViewModel(useCase, scheduler)
    }
}