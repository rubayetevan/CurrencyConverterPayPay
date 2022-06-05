package com.codesignal.paypay.currencyconverter.repository

import android.content.Context
import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.models.LatestRates
import com.codesignal.paypay.currencyconverter.repository.local.LocalDataSource
import com.codesignal.paypay.currencyconverter.repository.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

import javax.inject.Inject

class Repository @Inject constructor(
    private val context : Context,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val externalScope:CoroutineScope
) {

    suspend fun getLatestRates(): Flow<Resource<LatestRates>> {
        return flow {
            emit(Resource.Loading<LatestRates>())

            val result = withContext(externalScope.coroutineContext){
                remoteDataSource.getLatestRates()
            }

            if(result is Resource.Success){
                if (result.data != null) {
                    emit(Resource.Success<LatestRates>(result.data))
                    withContext(externalScope.coroutineContext) {
                        // TODO: DB operation
                    }
                } else {
                    emit(Resource.Empty<LatestRates>())
                }
            }else if(result is Resource.Error){
                emit(Resource.Error<LatestRates>(result.message ?: "Error"))
            }
        }
    }
}