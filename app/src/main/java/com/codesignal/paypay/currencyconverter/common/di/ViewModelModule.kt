package com.codesignal.paypay.currencyconverter.common.di

import com.codesignal.paypay.currencyconverter.common.utility.Validators
import com.codesignal.paypay.currencyconverter.repository.Repository
import com.codesignal.paypay.currencyconverter.useCases.CurrencyRateUseCase
import com.codesignal.paypay.currencyconverter.useCases.CurrencyNameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideValidator():Validators{
        return Validators()
    }

    @Provides
    fun provideCurrencyRateUseCase(repository: Repository):CurrencyRateUseCase{
        return CurrencyRateUseCase(repository)
    }

    @Provides
    fun provideCurrencyNameUseCase(repository: Repository):CurrencyNameUseCase{
        return CurrencyNameUseCase(repository)
    }

}