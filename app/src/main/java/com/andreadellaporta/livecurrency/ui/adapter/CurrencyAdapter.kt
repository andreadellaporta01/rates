package com.andreadellaporta.livecurrency.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.andreadellaporta.livecurrency.R
import com.andreadellaporta.livecurrency.databinding.ItemRateBinding
import com.andreadellaporta.livecurrency.model.Rate
import com.andreadellaporta.livecurrency.ui.RateViewModel
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.item_rate.view.*


class CurrencyAdapter: RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    private lateinit var source: BehaviorSubject<Rate>
    private var rateList:MutableList<Rate> = arrayListOf()
    private lateinit var base:MutableLiveData<Rate>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRateBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_rate, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemId(position: Int): Long {
        val rate = rateList[position]
        return rate.country.hashCode().toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(rateList[position], base)
        holder.itemView.setOnClickListener {
            rateList.removeAt(position)
            notifyDataSetChanged()
            source.onNext(Rate(holder.itemView.rate_country.text.toString(), holder.itemView.rate_value.text.toString().toDouble()))
        }
    }

    override fun getItemCount(): Int {
        return rateList.size
    }

    fun updateRateList(rateList:MutableList<Rate>, baseRate: MutableLiveData<Rate>){
        this.base = baseRate
        this.rateList.clear()
        this.rateList.addAll(rateList)
        notifyDataSetChanged()
    }

    fun updateRateList(baseRate: MutableLiveData<Rate>){
        this.base = baseRate
        notifyDataSetChanged()
    }

    fun setSource(source: BehaviorSubject<Rate>) {
        this.source = source
    }

    class ViewHolder(private val binding: ItemRateBinding):RecyclerView.ViewHolder(binding.root){

        private val viewModel = RateViewModel()

        fun bind(rate:Rate, baseRate:MutableLiveData<Rate>){
            viewModel.bind(rate, baseRate.value!!)
            binding.viewModel = viewModel
        }


    }

}