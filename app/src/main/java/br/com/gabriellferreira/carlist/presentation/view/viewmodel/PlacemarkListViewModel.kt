package br.com.gabriellferreira.carlist.presentation.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.gabriellferreira.carlist.domain.model.NetworkState
import br.com.gabriellferreira.carlist.domain.model.Placemark
import br.com.gabriellferreira.carlist.domain.model.Retryable
import br.com.gabriellferreira.carlist.domain.usecase.PlacemarkUseCase
import com.google.maps.android.clustering.ClusterManager
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class PlacemarkListViewModel @Inject constructor(
    private val useCase: PlacemarkUseCase
) : ViewModel() {

    var itemList: MutableLiveData<List<Placemark>> = MutableLiveData()
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    init {
        fetchPlacemarkList()
    }

    private fun fetchPlacemarkList() {
        useCase.fetchPlacemarkList()
            .subscribeOn(Schedulers.io())
            .subscribe(object : DisposableSingleObserver<List<Placemark>>() {
                override fun onSuccess(t: List<Placemark>) {
                    itemList.postValue(t)
                    onNetworkStateLoaded()
                }

                override fun onError(e: Throwable) {
                    onNetworkStateFailed(object : Retryable {
                        override fun retry() {
                            fetchPlacemarkList()
                        }
                    })
                }
            })
    }

    private fun onNetworkStateLoaded() {
        networkState.postValue(NetworkState(NetworkState.State.LOADED))
    }

    private fun onNetworkStateFailed(retryable: Retryable) {
        networkState.postValue(NetworkState(NetworkState.State.ERROR, retryable))
    }
}

