package com.codesignal.paypay.currencyconverter.common.di

import android.content.Context
import android.content.SharedPreferences
import com.codesignal.paypay.currencyconverter.repository.Repository
import com.codesignal.paypay.currencyconverter.repository.local.LocalDataSource
import com.codesignal.paypay.currencyconverter.repository.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope


@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    fun provideRepository(
        @ApplicationContext appContext: Context,
        externalScope: CoroutineScope,
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        sharedPreferences: SharedPreferences
    ): Repository {
        return Repository(appContext,localDataSource,remoteDataSource,externalScope,sharedPreferences)
    }

}