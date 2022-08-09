package com.codesignal.paypay.currencyconverter.common.di

import com.codesignal.paypay.currencyconverter.common.utility.Validators
import com.codesignal.paypay.currencyconverter.repository.Repository
import com.codesignal.paypay.currencyconverter.repository.local.LocalDataSource
import com.codesignal.paypay.currencyconverter.repository.remote.RemoteDataSource
import com.codesignal.paypay.currencyconverter.useCases.CurrencyConvertUseCase
import com.codesignal.paypay.currencyconverter.useCases.CurrencyNameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineScope


@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideValidator():Validators{
        return Validators()
    }

    @Provides
    fun provideCurrencyConvertUseCase(repository: Repository):CurrencyConvertUseCase{
        return CurrencyConvertUseCase(repository)
    }

    @Provides
    fun provideCurrencyNameUseCase(repository: Repository):CurrencyNameUseCase{
        return CurrencyNameUseCase(repository)
    }

}