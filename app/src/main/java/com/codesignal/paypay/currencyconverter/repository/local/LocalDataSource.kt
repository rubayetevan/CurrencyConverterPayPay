package com.codesignal.paypay.currencyconverter.repository.local

import android.content.SharedPreferences
import android.media.UnsupportedSchemeException
import com.codesignal.paypay.currencyconverter.common.utility.KEY_DB_UPDATE
import com.codesignal.paypay.currencyconverter.common.utility.KEY_DB_UPDATE_TIME
import com.codesignal.paypay.currencyconverter.models.CurrencyModel
import java.util.*
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val currencyModelDao: CurrencyModelDao,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun insertAllCurrencies(currencies: List<CurrencyModel>) {
        currencyModelDao.insertAll(currencies)
    }

    suspend fun getAllCurrencies(): MutableList<CurrencyModel> = currencyModelDao.getAllCurrencies()
    suspend fun getAllCurrencyNames(): List<String> = currencyModelDao.getAllCurrencyNames()

    fun getDbUpdateTime(): Date {
        return Date(sharedPreferences.getLong(KEY_DB_UPDATE_TIME, 0))
    }

    fun saveDbUpdateTime(date: Date) {
        saveDataSharedPref(KEY_DB_UPDATE_TIME, date.time)
    }

    private fun saveDataSharedPref(key: String, data: Any) {
        when (data) {
            is Long -> {
                sharedPreferences.edit().putLong(key, data).apply()
            }
            is Int -> {
                sharedPreferences.edit().putInt(key, data).apply()
            }
            is Float -> {
                sharedPreferences.edit().putFloat(key, data).apply()
            }
            is String -> {
                sharedPreferences.edit().putString(key, data).apply()
            }
            is Boolean -> {
                sharedPreferences.edit().putBoolean(key, data).apply()
            }
            else -> {
                throw UnsupportedOperationException("Data type is not supported}")
            }
        }
    }

    fun getDBInitializedState():Boolean{
        return sharedPreferences.getBoolean(KEY_DB_UPDATE,false);
    }

    fun savedBInitializedState(value: Boolean){
        saveDataSharedPref(KEY_DB_UPDATE,value)
    }

}