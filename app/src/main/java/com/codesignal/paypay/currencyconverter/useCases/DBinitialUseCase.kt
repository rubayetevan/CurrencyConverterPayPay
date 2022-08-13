package com.codesignal.paypay.currencyconverter.useCases

import com.codesignal.paypay.currencyconverter.common.utility.DB_UPDATE_TH_MIN
import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.models.CurrencyModel
import com.codesignal.paypay.currencyconverter.repository.Repository
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

class DBinitialUseCase @Inject constructor(private val repository: Repository) {

    fun shouldUpdateDB(): Boolean {
        val dbUpdatedTime = getDbUpdateTime()
        val currentTime = Date(System.currentTimeMillis())
        val diff: Long = currentTime.time - dbUpdatedTime.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        return minutes >= DB_UPDATE_TH_MIN
    }

    fun getDbUpdateTime() = repository.getDbUpdateTime()

    fun getDbInitializationState() = repository.getDbInitializationState()

    suspend fun updateOrInitializeDB(): Flow<Resource<List<CurrencyModel>>> {
        return channelFlow {
            if(shouldUpdateDB()){
                repository.updateOrInitializeDB().collect{ resource->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let {
                                send(Resource.Success(it))
                            }
                        }
                        is Resource.Error -> {
                            resource.message?.let {
                                send(Resource.Error(it))
                            }
                        }
                        is Resource.Loading -> {
                            send(Resource.Loading())
                        }
                        is Resource.Empty -> {
                            send(Resource.Empty())
                        }
                    }
                }
            }else{
                send(Resource.Error(message = "Can't update database in less than $DB_UPDATE_TH_MIN minutes of last update!"))
            }
        }
    }
}