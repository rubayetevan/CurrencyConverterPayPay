package com.codesignal.paypay.currencyconverter.useCases

import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.models.CurrencyModel
import com.codesignal.paypay.currencyconverter.repository.Repository
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

class CurrencyRateUseCaseTest {

    private val repository: Repository = mock()
    private lateinit var currencyRateUseCase: CurrencyRateUseCase

    @Before
    fun setUp() {
        currencyRateUseCase = CurrencyRateUseCase(repository)
    }

//    @Test
//    fun getLatestRatesSuccessTest() {
//        val bdt = CurrencyModel(
//            name = "BDT",
//            value = 95.019544
//        )
//
//        val usd = CurrencyModel(
//            name = "USD",
//            value = 1.0
//        )
//
//        val expected = listOf<CurrencyModel>(bdt, usd)
//
//        repository.stub {
//            onBlocking { getLatestRates() }.thenReturn(flow {
//                emit(Resource.Loading())
//                emit(Resource.Success(data = expected))
//            })
//        }
//
//        runBlocking {
//            val firstItem = currencyRateUseCase.getLatestRates().first()
//            assertTrue(firstItem is Resource.Loading)
//            val secondItem = repository.getLatestRates().drop(1).first()
//            assertTrue(secondItem is Resource.Success)
//            assertFalse(secondItem.data.isNullOrEmpty())
//            assertTrue(secondItem.data is List<CurrencyModel>)
//            assertTrue(2 == secondItem.data?.size)
//        }
//    }
//
//    @Test
//    fun getLatestRatesEmptyTest() {
//        repository.stub {
//            onBlocking { getLatestRates() }.thenReturn(flow {
//                emit(Resource.Loading())
//                emit(Resource.Empty())
//            })
//        }
//
//        runBlocking {
//            val firstItem = currencyRateUseCase.getLatestRates().first()
//            assertTrue(firstItem is Resource.Loading)
//            val secondItem = repository.getLatestRates().drop(1).first()
//            assertTrue(secondItem is Resource.Empty)
//            assertTrue(secondItem.data.isNullOrEmpty())
//        }
//    }
//
//    @Test
//    fun getLatestRatesErrorTest() {
//        val msg = "Couldn't get the latest rates"
//        repository.stub {
//            onBlocking { getLatestRates() }.thenReturn(flow {
//                emit(Resource.Loading())
//                emit(Resource.Error(msg))
//            })
//        }
//
//        runBlocking {
//            val firstItem = currencyRateUseCase.getLatestRates().first()
//            assertTrue(firstItem is Resource.Loading)
//            val secondItem = repository.getLatestRates().drop(1).first()
//            assertTrue(secondItem is Resource.Error)
//            assertTrue(secondItem.data.isNullOrEmpty())
//            assertEquals(msg, secondItem.message)
//        }
//    }

    @Test
    fun getConvertedCurrencyRatesSuccessTest() {
        val bdt = CurrencyModel(
            name = "BDT",
            value = 95.019544
        )

        val usd = CurrencyModel(
            name = "USD",
            value = 1.0
        )

        val aed = CurrencyModel(
            name = "AED",
            value = 3.673
        )

        val currencyList = listOf<CurrencyModel>(bdt, usd, aed)

        repository.stub {
            onBlocking { getLatestRates() }.thenReturn(flow {
                emit(Resource.Loading())
                emit(Resource.Success(data = currencyList))
            })
        }

        runBlocking {
            val firstItem = currencyRateUseCase.getConvertedCurrencyRates(aed.name, "1").first()
            assertTrue(firstItem is Resource.Loading)
            val secondItem =
                currencyRateUseCase.getConvertedCurrencyRates(aed.name, "1").drop(1).first()
            assertTrue(secondItem is Resource.Success)
            assertFalse(secondItem.data.isNullOrEmpty())
            assertEquals(currencyList.size, secondItem.data?.size)
            val expected: CurrencyModel? = secondItem.data?.single { s -> s.name == aed.name }
            assertEquals(1.00, expected?.value)
        }


    }

    @Test
    fun getConvertedCurrencyRatesErrorTest() {
        val bdt = CurrencyModel(
            name = "BDT",
            value = 95.019544
        )

        val usd = CurrencyModel(
            name = "USD",
            value = 1.0
        )

        val aed = CurrencyModel(
            name = "AED",
            value = 3.673
        )

        val currencyList = listOf<CurrencyModel>(bdt, usd, aed)

        repository.stub {
            onBlocking { getLatestRates() }.thenReturn(flow {
                emit(Resource.Loading())
                emit(Resource.Success(data = currencyList))
            })
        }

        runBlocking {
            val firstItem = currencyRateUseCase.getConvertedCurrencyRates("ALL", "1").first()
            assertTrue(firstItem is Resource.Loading)
            val secondItem =
                currencyRateUseCase.getConvertedCurrencyRates("ALL", "1").drop(1).first()
            assertTrue(secondItem is Resource.Error)
            assertFalse(secondItem.message.isNullOrBlank())
        }
    }


}