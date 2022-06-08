package com.codesignal.paypay.currencyconverter.common.di

import android.content.SharedPreferences
import com.codesignal.paypay.currencyconverter.common.api.OpenExchangeRatesApi
import com.codesignal.paypay.currencyconverter.repository.local.CurrencyModelDao
import com.codesignal.paypay.currencyconverter.repository.local.LocalDataSource
import com.codesignal.paypay.currencyconverter.repository.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        openExchangeRatesApi: OpenExchangeRatesApi
    ): RemoteDataSource {
        return RemoteDataSource(openExchangeRatesApi)
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(
        currencyModelDao: CurrencyModelDao,
        sharedPreferences: SharedPreferences
    ): LocalDataSource {
        return LocalDataSource(currencyModelDao,sharedPreferences)
    }
}