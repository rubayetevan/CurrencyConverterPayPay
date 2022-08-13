package com.codesignal.paypay.currencyconverter.repository

import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.models.CurrencyModel
import com.codesignal.paypay.currencyconverter.repository.local.LocalDataSource
import com.codesignal.paypay.currencyconverter.repository.remote.RemoteDataSource
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
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
        return channelFlow {
            send(Resource.Loading())
            if (currencies.isNullOrEmpty()) {
                val localData = withContext(externalScope.coroutineContext) {
                    localDataSource.getAllCurrencyModel()
                }
                if (!localData.isNullOrEmpty())
                    currencies = localData as MutableList<CurrencyModel>
                if (currencies.isEmpty()) {
                    send(Resource.Empty())
                } else {
                    val data = Collections.unmodifiableList(currencies)
                    send(Resource.Success(data = data))
                }
            } else {
                val data = Collections.unmodifiableList(currencies)
                send(Resource.Success(data = data))
            }
        }
    }

    suspend fun getAllCurrencyNames(): Flow<Resource<List<String>>> {
        return channelFlow {
            send(Resource.Loading())
            if (currencyNames.isEmpty()) {
                currencyNames = withContext(externalScope.coroutineContext) {
                    localDataSource.getAllCurrencyNames()
                }
                if (currencyNames.isEmpty()) {
                    send(Resource.Empty())
                } else if (currencyNames.isNotEmpty()) {
                    send(Resource.Success(data = currencyNames))
                } else {
                    send(Resource.Error(message = "Can not get currency names."))
                }
            } else {
                send(Resource.Success(data = currencyNames))
            }
        }
    }

    suspend fun updateOrInitializeDB(): Flow<Resource<List<CurrencyModel>>> {
        return channelFlow {
            send(Resource.Loading())
            val result = withContext(externalScope.coroutineContext) {
                remoteDataSource.getLatestRates()
            }

            if (result is Resource.Success) {
                val rates: JsonObject? = result.data?.getAsJsonObject("rates")

                rates?.keySet()?.let {
                    currencyNames = it.toList()
                }

                rates?.keySet()?.forEach { key ->
                    val value = rates.get(key).toString()
                    currencies.add(CurrencyModel(name = key, value = value.toDouble()))
                }

                val data = Collections.unmodifiableList(currencies)

                if (data.isEmpty()) {
                    send(Resource.Empty())
                } else {
                    withContext(externalScope.coroutineContext) {
                        localDataSource.insertAllCurrencies(currencies = data)
                    }
                    val date = Date(System.currentTimeMillis())
                    localDataSource.saveDbUpdateTime(date)
                    localDataSource.savedBInitializedState(true)
                    send(Resource.Success(data = data))
                }

            } else if (result is Resource.Error) {
                send(Resource.Error(result.message ?: "Could not get data."))
            } else if (result is Resource.Loading) {
                send(Resource.Loading())
            } else if (result is Resource.Empty) {
                send(Resource.Empty())
            }
        }
    }

    fun getDbUpdateTime() = localDataSource.getDbUpdateTime()

    fun getDbInitializationState() = localDataSource.getDBInitializedState()
}