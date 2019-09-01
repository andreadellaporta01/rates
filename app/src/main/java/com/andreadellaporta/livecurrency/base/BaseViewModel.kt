package com.andreadellaporta.livecurrency.base

import androidx.lifecycle.ViewModel
import com.andreadellaporta.livecurrency.di.component.DaggerViewModelInjector
import com.andreadellaporta.livecurrency.di.component.ViewModelInjector
import com.andreadellaporta.livecurrency.di.module.NetworkModule
import com.andreadellaporta.livecurrency.ui.RateListViewModel
import com.andreadellaporta.livecurrency.ui.RateViewModel

abstract class BaseViewModel : ViewModel() {

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is RateListViewModel -> injector.inject(this)
            is RateViewModel -> injector.inject(this)
        }
    }

}