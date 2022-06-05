package com.codesignal.paypay.currencyconverter.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codesignal.paypay.currencyconverter.models.LatestRates


@Database(entities = [LatestRates::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun latestRatesDao():LatestRatesDao
}