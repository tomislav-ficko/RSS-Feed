package com.ficko.rssfeed.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    val anyUseCaseInProgress = MutableLiveData<Boolean>()
    val anyUseCaseFailure = MutableLiveData<Throwable>()

    protected fun executeUseCase(
        inProgress: ((Boolean) -> Unit)? = null,
        onFailure: ((Throwable) -> Unit)? = null,
        useCase: suspend () -> Unit
    ) {
        viewModelScope.launch {
            try {
                inProgress?.let { it(true) } ?: postGenericProgress(true)
                useCase()
            } catch (e: Throwable) {
                onFailure?.let { it(e) } ?: postGenericFailure(e)
            } finally {
                inProgress?.let { it(false) } ?: postGenericProgress(false)
            }
        }
    }

    private fun postGenericProgress(inProgress: Boolean) = run { anyUseCaseInProgress.postValue(inProgress) }

    private fun postGenericFailure(e: Throwable) = run { anyUseCaseFailure.postValue(e) }
}