package com.codesignal.paypay.currencyconverter.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codesignal.paypay.currencyconverter.databinding.ListItemCurrencyBinding

class CurrencyAdapter(private val currencyList: List<String>,private val getCurrencyConvertedValue: (input: String) -> Unit) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            ListItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencyList[position]
        holder.bind(currency)
        holder.itemView.setOnClickListener {
            getCurrencyConvertedValue(currency)
        }
    }

    override fun getItemCount(): Int = currencyList.size

    inner class CurrencyViewHolder(private val binding: ListItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: String) {
            binding.currency = currency
        }
    }

}