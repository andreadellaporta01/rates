package com.andreadellaporta.livecurrency.ui

import androidx.lifecycle.MutableLiveData
import com.andreadellaporta.livecurrency.base.BaseViewModel
import com.andreadellaporta.livecurrency.model.Rate
import java.util.*
import kotlin.math.roundToInt

class RateViewModel : BaseViewModel() {

    private var rateCountry = String()
    private val rateValue = MutableLiveData<Double>()
    private var baseRateValue : Double = 0.0
    private var baseRateCountry = String()

    fun bind(rate: Rate, baseRate: Rate){
        rateCountry = rate.country
        baseRateValue= baseRate.value
        baseRateCountry = baseRate.country

        if(rateCountry == baseRateCountry)
            rateValue.value = baseRateValue
        else
            rateValue.value = ((rate.value * baseRateValue) * 1000).roundToInt() / 1000.0
    }

    fun getRateCountry():String{
        return rateCountry
    }

    fun getRateValue():MutableLiveData<Double>{
        return rateValue
    }

    fun getCountryCode():String{
        return rateCountry.take(2)
    }

    fun getRateFullName():String{
        val currency : Currency = Currency.getInstance(rateCountry)
        return currency.displayName
    }

}