package br.com.gabriellferreira.carlist.presentation.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.gabriellferreira.carlist.domain.model.Coordinates
import br.com.gabriellferreira.carlist.domain.model.NetworkState
import br.com.gabriellferreira.carlist.domain.model.Placemark
import br.com.gabriellferreira.carlist.domain.usecase.PlacemarkUseCase
import br.com.gabriellferreira.carlist.presentation.di.*
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Scheduler
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import javax.inject.Inject

class PlacemarkListViewModelTest {

    @Rule
    @JvmField
    var instantTaskRule: InstantTaskExecutorRule? = InstantTaskExecutorRule()

    @Mock
    lateinit var useCase: PlacemarkUseCase
    @Inject
    lateinit var scheduler: Scheduler
    @Inject
    lateinit var viewModel: PlacemarkListViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val component = DaggerTestAppComponent.builder()
            .appModule(TestAppModule(AppApplication()))
            .repositoryModule(TestRepositoryModule())
            .testServiceModule(TestServiceModule())
            .build()
        component.inject(this)

        viewModel = PlacemarkListViewModel(useCase, scheduler)
    }

    @Test
    fun onFetchPlacemarkList_shouldReturnProgressState() {
        // given
        whenever(useCase.fetchPlacemarkList())
            .thenReturn(
                Single.just(listOf())
            )

        //when
        viewModel.fetchPlacemarkList()

        //then
        val result = viewModel.itemList.value
        assertThat(result is NetworkState.InProgress)
    }

    @Test
    fun onFetchPlacemarkList_shouldReturnErrorState() {

        // given
        whenever(useCase.fetchPlacemarkList())
            .thenReturn(
                Single.error(Exception())
            )

        //when
        viewModel.fetchPlacemarkList()

        //then
        val result = viewModel.itemList.value
        assertThat(result is NetworkState.Error)
    }

    @Test
    fun onFetchPlacemarkList_shouldReturnSuccessState() {
        // given
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
                        ),
                        Placemark(
                            engineType = "eletric",
                            coordinates = Coordinates(0.0, 0.0),
                            address = "Kieler Strasse",
                            fuelString = "Fuel: 45%",
                            name = "EIC-6727"
                        ),
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

        //when
        viewModel.fetchPlacemarkList()

        //then
        val result = viewModel.itemList.value
        assertThat(result is NetworkState.Loaded<List<Placemark>>)
        assertThat((result as NetworkState.Loaded<List<Placemark>>).result.size).isEqualTo(3)
    }
}