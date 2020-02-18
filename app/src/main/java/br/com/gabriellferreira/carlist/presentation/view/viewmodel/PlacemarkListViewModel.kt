package br.com.gabriellferreira.carlist.presentation.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.gabriellferreira.carlist.domain.model.NetworkState
import br.com.gabriellferreira.carlist.domain.model.Placemark
import br.com.gabriellferreira.carlist.domain.model.Retryable
import br.com.gabriellferreira.carlist.domain.usecase.PlacemarkUseCase
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class PlacemarkListViewModel @Inject constructor(
    private val useCase: PlacemarkUseCase
) : ViewModel() {

    var itemList: MutableLiveData<NetworkState<List<Placemark>>> = MutableLiveData()

    init {
        fetchPlacemarkList()
    }

    private fun fetchPlacemarkList() {
        itemList.postValue(NetworkState.InProgress)
        useCase.fetchPlacemarkList()
            .subscribeOn(Schedulers.io())
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
}

