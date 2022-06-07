package com.codesignal.paypay.currencyconverter.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codesignal.paypay.currencyconverter.models.CurrencyModel

@Dao
interface CurrencyModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyModel>)

    @Query("SELECT * FROM currencies")
    suspend fun getAllCurrencies(): MutableList<CurrencyModel>
}