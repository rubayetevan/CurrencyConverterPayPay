package com.codesignal.paypay.currencyconverter.repository

import android.content.Context
import com.codesignal.paypay.currencyconverter.repository.local.LocalDataSource
import com.codesignal.paypay.currencyconverter.repository.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class Repository @Inject constructor(
    private val context : Context,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val externalScope:CoroutineScope
) {

    // TODO: Will be implemented later.
}