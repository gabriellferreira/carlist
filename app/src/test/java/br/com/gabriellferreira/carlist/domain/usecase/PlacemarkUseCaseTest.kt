package br.com.gabriellferreira.carlist.domain.usecase

import br.com.gabriellferreira.carlist.domain.model.Placemark
import br.com.gabriellferreira.carlist.domain.repository.PlacemarkRepository
import br.com.gabriellferreira.carlist.presentation.di.*
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PlacemarkUseCaseTest {

    @Mock
    lateinit var repository: PlacemarkRepository

    lateinit var useCase: PlacemarkUseCase

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val component = DaggerTestAppComponent.builder()
            .appModule(TestAppModule(AppApplication()))
            .repositoryModule(TestRepositoryModule())
            .testServiceModule(TestServiceModule())
            .build()
        component.inject(this)
        useCase = PlacemarkUseCase(repository)
    }

    @Test
    fun onFetchPlacemarkList_listWithOneCar_resultOk() {
        //given
        whenever(repository.fetchPlacemarkList())
            .thenReturn(
                Single.just(
                    listOf(
                        Placemark(
                            engineType = "abcd",
                            coordinates = Triple(0.0, 0.0, 0.0),
                            address = data.address ?: "",
                            fuel = data.fuel ?: -1,
                            name = data.name ?: ""
                        )
                    )
                )
            )

        //when
        val result = useCase.fetchPlacemarkList().blockingGet()

        //then
        assertThat(result.size).isEqualTo(1)
    }
}