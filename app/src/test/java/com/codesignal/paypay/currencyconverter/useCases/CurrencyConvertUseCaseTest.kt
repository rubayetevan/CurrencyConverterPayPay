package com.codesignal.paypay.currencyconverter.useCases

import android.content.SharedPreferences
import com.codesignal.paypay.currencyconverter.repository.local.CurrencyModelDao
import com.codesignal.paypay.currencyconverter.repository.local.LocalDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class CurrencyConvertUseCaseTest {

    private val currencyModelDao : CurrencyModelDao = mock(CurrencyModelDao::class.java)
    private val sharedPreferences : SharedPreferences = mock(SharedPreferences::class.java)
    private lateinit var localDataSource: LocalDataSource

    @Before
    fun setup(){
        localDataSource = LocalDataSource(currencyModelDao,sharedPreferences)
    }

    @Test
    fun getConvertedCurrency() {
        runBlocking {
            val currencies = listOf("BDT", "USD", "AUD")
            Mockito.`when`(currencyModelDao. getAllCurrencyNames()).thenReturn(currencies)
            val result = localDataSource.getAllCurrencyNames()
            assertEquals(currencies,result)
        }
    }

    @Test
    fun getLatestRates() {
    }
}