package com.codesignal.paypay.currencyconverter.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codesignal.paypay.currencyconverter.databinding.ListItemCurrencyBinding
import com.codesignal.paypay.currencyconverter.models.CurrencyModel

class CurrencyAdapter() :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private var currencyList: MutableList<CurrencyModel> = ArrayList<CurrencyModel>()

    fun setData(data: List<CurrencyModel>) {
        currencyList = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            ListItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind("${
            String.format("%.2f",
                currencyList[position].value)
        } ${currencyList[position].name}")
    }

    override fun getItemCount(): Int = currencyList.size

    inner class CurrencyViewHolder(private val binding: ListItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: String) {
            binding.currency = currency
        }
    }

}