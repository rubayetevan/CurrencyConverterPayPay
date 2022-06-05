package com.codesignal.paypay.currencyconverter.repository.local

import androidx.room.*
import com.codesignal.paypay.currencyconverter.models.LatestRates

@Dao
interface LatestRatesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLatestRate(data: LatestRates)

    @Update
    suspend fun updateLatestRate(data: LatestRates)

    @Delete
    suspend fun deleteLatestRate(data: LatestRates)

}