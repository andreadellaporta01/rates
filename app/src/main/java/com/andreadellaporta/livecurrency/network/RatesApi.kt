package com.andreadellaporta.livecurrency.network

import com.andreadellaporta.livecurrency.model.RateList
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApi {

    @GET("/latest?base=EUR")
    fun getRates(@Query("base") base: String): Observable<RateList>

}