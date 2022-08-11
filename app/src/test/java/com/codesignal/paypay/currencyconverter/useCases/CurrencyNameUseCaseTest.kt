package com.codesignal.paypay.currencyconverter.useCases

import com.codesignal.paypay.currencyconverter.common.utility.Resource
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

class CurrencyNameUseCaseTest {
    private val repository :Repository = mock()
    private lateinit var currencyNameUseCase:CurrencyNameUseCase

    @Before
    fun setUp() {
        currencyNameUseCase = CurrencyNameUseCase(repository)
    }

    @Test
    fun getAllCurrencyNamesSuccessTest() {
        val expected = listOf("BDT", "USD", "AUD")
        repository.stub {
            onBlocking { repository.getAllCurrencyNames() }.thenReturn(flow {
                emit(Resource.Loading())
                emit(Resource.Success(data = expected))
            })
        }

        runBlocking {
            val first = currencyNameUseCase.getAllCurrencyNames().first()
            assertTrue(first is Resource.Loading)
            val second = currencyNameUseCase.getAllCurrencyNames().drop(1).first()
            assertTrue(second is Resource.Success)
            assertFalse(second.data.isNullOrEmpty())
            assertEquals(expected,second.data)
        }
    }

    @Test
    fun getAllCurrencyNamesEmptyTest() {
        repository.stub {
            onBlocking { repository.getAllCurrencyNames() }.thenReturn(flow {
                emit(Resource.Loading())
                emit(Resource.Empty())
            })
        }

        runBlocking {
            val first = currencyNameUseCase.getAllCurrencyNames().first()
            assertTrue(first is Resource.Loading)
            val second = currencyNameUseCase.getAllCurrencyNames().drop(1).first()
            assertTrue(second is Resource.Empty)
        }
    }
}