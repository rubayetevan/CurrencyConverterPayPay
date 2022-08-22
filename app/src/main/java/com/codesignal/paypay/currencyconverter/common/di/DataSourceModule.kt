package com.codesignal.paypay.currencyconverter.common.di

import android.content.Context
import android.content.SharedPreferences
import com.codesignal.paypay.currencyconverter.common.api.OpenExchangeRatesApi
import com.codesignal.paypay.currencyconverter.common.utility.SHARED_PREF_NAME
import com.codesignal.paypay.currencyconverter.repository.Repository
import com.codesignal.paypay.currencyconverter.repository.local.CurrencyModelDao
import com.codesignal.paypay.currencyconverter.repository.local.LocalDataSource
import com.codesignal.paypay.currencyconverter.repository.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        openExchangeRatesApi: OpenExchangeRatesApi,
    ): RemoteDataSource {
        return RemoteDataSource(openExchangeRatesApi)
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(
        currencyModelDao: CurrencyModelDao,
        sharedPreferences: SharedPreferences,
    ): LocalDataSource {
        return LocalDataSource(currencyModelDao, sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideRepository(
        externalScope: CoroutineScope,
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
    ): Repository {
        return Repository(localDataSource, remoteDataSource, externalScope)
    }


}