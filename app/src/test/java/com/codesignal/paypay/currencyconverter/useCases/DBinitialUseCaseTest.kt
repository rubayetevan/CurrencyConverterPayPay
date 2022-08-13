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
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import java.util.*

class DBinitialUseCaseTest {

    private val repository: Repository = mock()
    private lateinit var dBinitialUseCase: DBinitialUseCase

    @Before
    fun setup() {
        dBinitialUseCase = DBinitialUseCase(repository)
    }

    @Test
    fun getDbUpdateTimeTest() {
        val expected = Date(System.currentTimeMillis())
        Mockito.`when`(repository.getDbUpdateTime()).thenReturn(expected)
        val actual = dBinitialUseCase.getDbUpdateTime()
        assertEquals(expected, actual)
    }

    @Test
    fun getDbInitializationStatePositiveTest() {
        val expected = true
        Mockito.`when`(repository.getDbInitializationState()).thenReturn(expected)
        val actual = dBinitialUseCase.getDbInitializationState()
        assertEquals(expected, actual)
    }

    @Test
    fun getDbInitializationStateNegativeTest() {
        val expected = false
        Mockito.`when`(repository.getDbInitializationState()).thenReturn(expected)
        val actual = dBinitialUseCase.getDbInitializationState()
        assertEquals(expected, actual)
    }

    @Test
    fun shouldUpdateDbPositiveTest() {
        val expected = Date(System.currentTimeMillis())
        Mockito.`when`(repository.getDbUpdateTime()).thenReturn(expected)
    }

    @Test
    fun shouldUpdateDbNegativeTest() {
        val expected = Date(System.currentTimeMillis())
        Mockito.`when`(repository.getDbUpdateTime()).thenReturn(expected)

    }

    @Test
    fun updateOrInitializeDbSuccessTest(){
        val expected = Date(1550387992465)
        Mockito.`when`(repository.getDbUpdateTime()).thenReturn(expected)

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
            onBlocking { updateOrInitializeDB() }.thenReturn(
                flow {
                    emit(Resource.Loading())
                    emit(Resource.Success(currencyList))
                }
            )
        }

        runBlocking {
           val first =  dBinitialUseCase.updateOrInitializeDB().first()
            assertTrue(first is Resource.Loading)
            val second =  dBinitialUseCase.updateOrInitializeDB().drop(1).first()
            assertTrue(second is Resource.Success)
            assertEquals(currencyList,second.data)
        }

    }

    @Test
    fun updateOrInitializeDbErrorTest(){
        val expected = Date(System.currentTimeMillis())
        Mockito.`when`(repository.getDbUpdateTime()).thenReturn(expected)

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
            onBlocking { updateOrInitializeDB() }.thenReturn(
                flow {
                    emit(Resource.Loading())
                    emit(Resource.Success(currencyList))
                }
            )
        }

        runBlocking {
            val first =  dBinitialUseCase.updateOrInitializeDB().first()
            assertTrue(first is Resource.Error)
            assertFalse(first.message.isNullOrEmpty())
        }

    }

}