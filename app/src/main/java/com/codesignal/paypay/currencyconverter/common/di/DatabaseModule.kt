package com.codesignal.paypay.currencyconverter.common.di

import android.content.Context
import androidx.room.Room
import com.codesignal.paypay.currencyconverter.repository.local.AppDatabase
import com.codesignal.paypay.currencyconverter.repository.local.CurrencyModelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "paypay"
        ).setQueryCallback(
            { sqlQuery, bindArgs ->
                println("SQL Query: $sqlQuery SQL Args: $bindArgs")
            },
            Executors.newSingleThreadExecutor()
        ).build()
    }

    @Provides
    fun provideCurrencyModelDao(appDatabase: AppDatabase): CurrencyModelDao {
        return appDatabase.currencyModelDao()
    }
}