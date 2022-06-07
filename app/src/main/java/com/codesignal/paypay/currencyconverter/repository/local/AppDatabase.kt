package com.codesignal.paypay.currencyconverter.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codesignal.paypay.currencyconverter.models.CurrencyModel


@Database(entities = [CurrencyModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyModelDao():CurrencyModelDao
}