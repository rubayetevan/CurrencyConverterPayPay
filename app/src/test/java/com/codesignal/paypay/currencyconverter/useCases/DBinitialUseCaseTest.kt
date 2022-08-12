package com.codesignal.paypay.currencyconverter.useCases

import com.codesignal.paypay.currencyconverter.repository.Repository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
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
        val dbUpdateTime = Date(1500241092536)
        Mockito.`when`(repository.getDbUpdateTime()).thenReturn(dbUpdateTime)
        val actual = dBinitialUseCase.shouldUpdateDB()
        assertTrue(actual)
    }

    @Test
    fun shouldUpdateDbNegativeTest() {
        val dbUpdateTime = Date(System.currentTimeMillis())
        Mockito.`when`(repository.getDbUpdateTime()).thenReturn(dbUpdateTime)
        val actual = dBinitialUseCase.shouldUpdateDB()
        assertFalse(actual)
    }

}