package com.codesignal.paypay.currencyconverter.useCases

import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.models.CurrencyModel
import com.codesignal.paypay.currencyconverter.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class CurrencyRateUseCase @Inject constructor(private val repository: Repository) {

    suspend fun getConvertedCurrencyRates(
        from: String,
        currencyValue: String,
    ): Flow<Resource<List<CurrencyModel>>> {
        return flow {

            val value: Double = if (currencyValue.isBlank() || currencyValue.isEmpty()) 0.00
            else currencyValue.trim().toDouble()

            repository.getLatestRates().collect { it ->
                when (it) {
                    is Resource.Success<List<CurrencyModel>> -> {
                        val currencies = it.data
                        val fromValue: Double? = currencies?.find { cur -> cur.name == from }?.value
                        val results: MutableList<CurrencyModel> = ArrayList<CurrencyModel>()
                        fromValue?.let {
                            currencies.forEach { currency ->
                                val toValue: Double = currency.value
                                var fromUsd = 1.0 / fromValue
                                fromUsd *= value
                                val convertedValue = toValue * fromUsd
                                results.add(CurrencyModel(name = currency.name,
                                    value = convertedValue))
                            }
                            val data = Collections.unmodifiableList(results)
                            if (data.isEmpty()) {
                                emit(Resource.Empty())
                            } else {
                                emit(Resource.Success(data = data))
                            }
                        } ?: run {
                            emit(Resource.Error(message = "Can't convert Value"))
                        }
                    }
                    is Resource.Error -> {
                        emit(Resource.Error(it.message ?: "Could not get data."))
                    }
                    is Resource.Loading -> {
                        emit(Resource.Loading<List<CurrencyModel>>())
                    }
                    is Resource.Empty->{
                        emit(Resource.Empty())
                    }
                }
            }
        }
    }
}