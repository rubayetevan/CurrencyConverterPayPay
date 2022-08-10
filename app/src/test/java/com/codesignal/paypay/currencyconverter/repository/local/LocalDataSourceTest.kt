package com.codesignal.paypay.currencyconverter.repository.local

import android.content.SharedPreferences
import com.codesignal.paypay.currencyconverter.common.utility.KEY_DB_UPDATE
import com.codesignal.paypay.currencyconverter.common.utility.KEY_DB_UPDATE_TIME
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class LocalDataSourceTest {

    private lateinit var localDataSource: LocalDataSource
    private val currencyModelDao : CurrencyModelDao = Mockito.mock(CurrencyModelDao::class.java)
    private val sharedPreferences : SharedPreferences = Mockito.mock(SharedPreferences::class.java)

    @Before
    fun setUp() {
        localDataSource = LocalDataSource(currencyModelDao,sharedPreferences)
    }

    @Test
    fun getDbUpdateTimeTest(){
        val expected = Date(System.currentTimeMillis())
        Mockito.`when`(sharedPreferences.getLong(KEY_DB_UPDATE_TIME, 0)).thenReturn(expected.time)
        val actual = localDataSource.getDbUpdateTime()
        assertEquals(expected,actual)
    }

    @Test
    fun getDBInitializedStatePositiveTest(){
        val expected = true
        Mockito.`when`(sharedPreferences.getBoolean(KEY_DB_UPDATE,false)).thenReturn(expected)
        val actual = localDataSource.getDBInitializedState()
        assertEquals(expected,actual)
    }

    @Test
    fun getDBInitializedStateNegativeTest(){
        val expected = true
        Mockito.`when`(sharedPreferences.getBoolean(KEY_DB_UPDATE,false)).thenReturn(expected)
        val actual = localDataSource.getDBInitializedState()
        assertEquals(expected,actual)
    }

    @Test
    fun getAllCurrencyNamesTest() {
        runBlocking {
            val expected = listOf("BDT", "USD", "AUD")
            Mockito.`when`(currencyModelDao. getAllCurrencyNames()).thenReturn(expected)
            val actual = localDataSource.getAllCurrencyNames()
            assertEquals(expected,actual)
        }
    }


}