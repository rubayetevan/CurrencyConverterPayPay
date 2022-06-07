package com.codesignal.paypay.currencyconverter.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.codesignal.paypay.currencyconverter.common.utility.Currencies
import com.codesignal.paypay.currencyconverter.databinding.ActivityMainBinding
import com.codesignal.paypay.currencyconverter.viewModels.MainViewModel
import com.codesignal.paypay.currencyconverter.views.adapters.CurrencyAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel
        val view = binding.root
        setContentView(view)

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(viewModel.fromCurrencyPosition!=position){
                    viewModel.fromCurrencyPosition = position
                    viewModel.getCurrencyConvertedValue()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        val currencyAdapter = CurrencyAdapter()
        binding.currencyRV.layoutManager = GridLayoutManager(this@MainActivity,2)
        binding.currencyRV.adapter = currencyAdapter
        lifecycleScope.launch {
            viewModel.result.collect{
                currencyAdapter.setData(it)
            }
        }
    }
}