package com.codesignal.paypay.currencyconverter.common.di

import com.codesignal.paypay.currencyconverter.common.api.OpenExchangeRatesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {
    @Singleton
    @Provides
    fun provideOpenExchangeRateApi(retrofit: Retrofit): OpenExchangeRatesApi {
        return retrofit.create(OpenExchangeRatesApi::class.java)
    }
}