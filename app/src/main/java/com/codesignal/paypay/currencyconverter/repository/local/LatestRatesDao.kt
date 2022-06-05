package com.codesignal.paypay.currencyconverter.repository.local

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.codesignal.paypay.currencyconverter.models.LatestRates

@Dao
interface LatestRatesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLatestRate(data: LatestRates)

    @Update
    suspend fun updateLatestRate(data: LatestRates)

    @Delete
    suspend fun deleteLatestRate(data: LatestRates)

    @RawQuery
    suspend fun getCurrencyValueBasedOnUSD(query: SupportSQLiteQuery): Double

}