package com.codesignal.paypay.currencyconverter.common.di

import com.codesignal.paypay.currencyconverter.views.adapters.CurrencyAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun provideCurrencyAdapter():CurrencyAdapter{
        return CurrencyAdapter()
    }
}