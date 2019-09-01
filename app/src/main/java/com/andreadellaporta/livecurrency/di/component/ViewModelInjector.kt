package com.andreadellaporta.livecurrency.di.component

import com.andreadellaporta.livecurrency.di.module.NetworkModule
import com.andreadellaporta.livecurrency.ui.RateListViewModel
import com.andreadellaporta.livecurrency.ui.RateViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {

    fun inject(rateListViewModel: RateListViewModel)
    fun inject(rateViewModel: RateViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}