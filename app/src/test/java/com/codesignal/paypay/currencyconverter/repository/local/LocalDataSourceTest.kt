package com.codesignal.paypay.currencyconverter.repository.local

import android.content.SharedPreferences
import com.codesignal.paypay.currencyconverter.common.utility.KEY_DB_UPDATE
import com.codesignal.paypay.currencyconverter.common.utility.KEY_DB_UPDATE_TIME
import com.codesignal.paypay.currencyconverter.models.CurrencyModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.*
import java.util.*

class LocalDataSourceTest {

    private lateinit var localDataSource: LocalDataSource
    private val currencyModelDao: CurrencyModelDao = Mockito.mock(CurrencyModelDao::class.java)
    private val sharedPreferences: SharedPreferences = Mockito.mock(SharedPreferences::class.java)

    @Before
    fun setUp() {
        localDataSource = LocalDataSource(currencyModelDao, sharedPreferences)
    }

    @Test
    fun insertAllCurrenciesTest(){
        val input = listOf<CurrencyModel>()
        currencyModelDao.stub {
            onBlocking { insertAll(input) }.thenReturn(anyOrNull())
        }
        runBlocking {
            currencyModelDao.insertAll(input)
            verify(currencyModelDao, times(1)).insertAll(input)
        }
    }

    @Test
    fun getDbUpdateTimeTest() {
        val expected = Date(System.currentTimeMillis())
        Mockito.`when`(sharedPreferences.getLong(KEY_DB_UPDATE_TIME, 0)).thenReturn(expected.time)
        val actual = localDataSource.getDbUpdateTime()
        assertEquals(expected, actual)
    }

    @Test
    fun getDBInitializedStatePositiveTest() {
        val expected = true
        Mockito.`when`(sharedPreferences.getBoolean(KEY_DB_UPDATE, false)).thenReturn(expected)
        val actual = localDataSource.getDBInitializedState()
        assertEquals(expected, actual)
    }

    @Test
    fun getDBInitializedStateNegativeTest() {
        val expected = false
        Mockito.`when`(sharedPreferences.getBoolean(KEY_DB_UPDATE, false)).thenReturn(expected)
        val actual = localDataSource.getDBInitializedState()
        assertEquals(expected, actual)
    }

    @Test
    fun getAllCurrencyNamesTest() {
        val expected = listOf("BDT", "USD", "AUD")
        currencyModelDao.stub {
            onBlocking { getAllCurrencyNames() }.thenReturn(expected)
        }
        runBlocking {

            val actual = localDataSource.getAllCurrencyNames()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun getAllCurrencyModels() {
        val bdt = CurrencyModel(
            name = "BDT",
            value = 95.019544
        )

        val usd = CurrencyModel(
            name = "USD",
            value = 1.0
        )

        val aed = CurrencyModel(
            name = "AED",
            value = 3.673
        )

        val expected: MutableList<CurrencyModel> = mutableListOf<CurrencyModel>(bdt, usd, aed)

        currencyModelDao.stub {
            onBlocking { getAllCurrencies() }.thenReturn(expected)
        }
        runBlocking {
            val actual = localDataSource.getAllCurrencyModel()
            assertEquals(expected, actual)
        }
    }


}