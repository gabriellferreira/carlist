package br.com.gabriellferreira.carlist.presentation.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.gabriellferreira.carlist.domain.model.Coordinates
import br.com.gabriellferreira.carlist.domain.model.NetworkState
import br.com.gabriellferreira.carlist.domain.model.Placemark
import br.com.gabriellferreira.carlist.domain.model.Retryable
import br.com.gabriellferreira.carlist.domain.usecase.PlacemarkUseCase
import io.reactivex.Scheduler
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

open class PlacemarkListViewModel @Inject constructor(
    private val useCase: PlacemarkUseCase,
    private val schedulers: Scheduler
) : ViewModel() {

    var itemList: MutableLiveData<NetworkState<List<Placemark>>> = MutableLiveData()

    fun fetchPlacemarkList() {
        itemList.postValue(NetworkState.InProgress)
        useCase.fetchPlacemarkList()
            .subscribeOn(schedulers)
            .subscribe(object : DisposableSingleObserver<List<Placemark>>() {
                override fun onSuccess(t: List<Placemark>) {
                    itemList.postValue(NetworkState.Loaded(t))
                }

                override fun onError(e: Throwable) {
                    itemList.postValue(NetworkState.Error(object : Retryable {
                        override fun retry() {
                            fetchPlacemarkList()
                        }
                    }))
                }
            })
    }

    fun getDefaultLocation(): Coordinates = useCase.getDefaultLocation()
    fun getDefaultZoomLevel(): Float = useCase.getDefaultZoomLevel()
}

