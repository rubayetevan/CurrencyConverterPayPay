package com.codesignal.paypay.currencyconverter.repository

import com.codesignal.paypay.currencyconverter.common.utility.DB_UPDATE_TH_MIN
import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.models.CurrencyModel
import com.codesignal.paypay.currencyconverter.repository.local.LocalDataSource
import com.codesignal.paypay.currencyconverter.repository.remote.RemoteDataSource
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val externalScope: CoroutineScope,
) {

    private var currencies: MutableList<CurrencyModel> = ArrayList<CurrencyModel>()
    private var currencyNames: List<String> = ArrayList<String>()

    suspend fun getLatestRates(): Flow<Resource<List<CurrencyModel>>> {
        return flow {
            if (currencies.isEmpty()) {
                emit(Resource.Loading())

                val result = withContext(externalScope.coroutineContext) {
                    remoteDataSource.getLatestRates()
                }

                if (result is Resource.Success) {

                    val rates: JsonObject? = result.data?.getAsJsonObject("rates")

                    rates?.keySet()?.let {
                        currencyNames =it.toList()
                    }

                    rates?.keySet()?.forEach { key ->
                        val value = rates.get(key).toString()
                        currencies.add(CurrencyModel(name = key, value = value.toDouble()))
                    }

                    val data = Collections.unmodifiableList(currencies)
                    withContext(externalScope.coroutineContext) {
                        localDataSource.insertAllCurrencies(currencies = data)
                    }

                    val date = Date(System.currentTimeMillis())
                    localDataSource.saveDbUpdateTime(date)
                    localDataSource.savedBInitializedState(true)

                    emit(Resource.Success(data = data))

                } else if (result is Resource.Error) {
                    emit(Resource.Error(result.message ?: "Could not get data."))
                }
            } else {
                val data = Collections.unmodifiableList(currencies)
                emit(Resource.Success(data = data))
            }
        }
    }



    suspend fun getAllCurrencyNames(): Flow<Resource<List<String>>> {
        return flow {
            emit(Resource.Loading())
            if (currencyNames.isEmpty()) {
                currencyNames = withContext(externalScope.coroutineContext) {
                    localDataSource.getAllCurrencyNames()
                }
                if (currencyNames.isEmpty()) {
                    emit(Resource.Empty())
                } else if (currencyNames.isNotEmpty()) {
                    emit(Resource.Success(data = currencyNames))
                } else {
                    emit(Resource.Error(message = "Can not get currency names."))
                }
            } else {
                emit(Resource.Success(data = currencyNames))
            }
        }
    }


    fun getDbUpdateTime() = localDataSource.getDbUpdateTime()

    fun getDbInitializationState() = localDataSource.getDBInitializedState()
}