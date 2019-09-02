package com.andreadellaporta.livecurrency.ui

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.andreadellaporta.livecurrency.R
import com.andreadellaporta.livecurrency.base.BaseViewModel
import com.andreadellaporta.livecurrency.model.Rate
import com.andreadellaporta.livecurrency.model.RateDao
import com.andreadellaporta.livecurrency.network.RatesApi
import com.andreadellaporta.livecurrency.ui.adapter.CurrencyAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class RateListViewModel(private val rateDao: RateDao) : BaseViewModel() {

    @Inject
    lateinit var ratesApi: RatesApi

    private lateinit var subscription: Disposable

    val errorMessage:MutableLiveData<Int> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadRates() }

    val currentBase = MutableLiveData<Rate>()
    val currencyAdapter: CurrencyAdapter = CurrencyAdapter()
    var base : MutableLiveData<Rate> = MutableLiveData()

    var baseCountryCode : MutableLiveData<String> = MutableLiveData()
    var baseCountry : MutableLiveData<String> = MutableLiveData()
    var baseFullName : MutableLiveData<String> = MutableLiveData()
    var baseValue : MutableLiveData<String> = MutableLiveData()

    val df = DecimalFormat("#.##")

    var source = BehaviorSubject.create<Rate>()
    val disposable = CompositeDisposable()

    init{
        base.value = Rate("EUR", df.parse("100")!!.toDouble())
        currentBase.value = base.value
        currencyAdapter.setHasStableIds(true)
        disposable.add(source.subscribe {
            base.value = it
            setBaseValue()
        })
        currencyAdapter.setSource(source)
        setBaseValue()
        loadRates()
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
        disposable.dispose()
    }

    private fun setBaseValue() {
        baseCountry.value = base.value!!.country
        baseCountryCode.value = base.value!!.country.take(2)
        baseFullName.value =  Currency.getInstance(base.value!!.country).displayName
        baseValue.value = base.value!!.value.toString()
    }


    private fun loadRates(){
        subscription = Observable
            .fromCallable{rateDao.all}
            .delay(1,TimeUnit.SECONDS)
            .repeat()
            .switchMap { dbRateList ->

                ratesApi.getRates(base.value!!.country.toUpperCase()).concatMap {
                        apiRateList ->
                    val convertedRateList = apiRateList.rates.map { (it) -> Rate(it, apiRateList.rates[it] ?: error(""))}.toMutableList()
                    if(dbRateList.isEmpty()){
                        rateDao.insertAll(convertedRateList)
                    } else {
                        rateDao.updateAll(convertedRateList)
                    }

                    Observable.just(convertedRateList)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveCurrencyStart() }
            .doOnTerminate { onRetrieveCurrencyFinish() }
            .subscribe (
                { result -> onRetrieveCurrencySuccess(result) },
                { onRetrieveCurrencyError() }
            )
    }

    private fun onRetrieveCurrencyStart(){
        errorMessage.value = null
    }

    private fun onRetrieveCurrencyFinish(){
    }

    private fun onRetrieveCurrencySuccess(rateList: MutableList<Rate>){
        currencyAdapter.updateRateList(rateList, base)
    }

    private fun onRetrieveCurrencyError(){
        errorMessage.value = R.string.post_error
    }

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val newBaseValue = if (s.isEmpty() || s.startsWith(".") || s.startsWith(","))
            0.0
        else {
            df.parse(s.toString())!!.toDouble()
        }

        base.value = Rate(base.value!!.country, newBaseValue)
        currencyAdapter.updateRateList(base)
    }

    fun getCountryCode():MutableLiveData<String>{
        return baseCountryCode
    }

    fun getRateCountry():MutableLiveData<String>{
        return baseCountry
    }

    fun getRateFullName():MutableLiveData<String>{
        return baseFullName
    }

    fun getRateValue():MutableLiveData<String>{
        return baseValue
    }

}