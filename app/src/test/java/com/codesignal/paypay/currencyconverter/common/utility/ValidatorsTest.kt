package com.codesignal.paypay.currencyconverter.common.utility

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


internal class ValidatorsTest {
    private lateinit var validators: Validators

    @Before
    fun setup() {
        validators = Validators()
    }

    @Test
    fun validateDecimalInputInvalidParamTest1() {
        val expected = false
        val actual = validators.validateDecimalInput(".")
        assertEquals(expected, actual)
    }

    @Test
    fun validateDecimalInputInvalidParamTest2() {
        val expected = false
        val actual = validators.validateDecimalInput("")
        assertEquals(expected, actual)
    }

    @Test
    fun validateDecimalInputInvalidParamTest3() {
        val expected = false
        val actual = validators.validateDecimalInput("   ")
        assertEquals(expected, actual)
    }

    @Test
    fun validateDecimalInputValidParamTest1() {
        val expected = true
        val actual = validators.validateDecimalInput(" .1")
        assertEquals(expected, actual)
    }

    @Test
    fun validateDecimalInputValidParamTest2() {
        val expected = true
        val actual = validators.validateDecimalInput("1.1 ")
        assertEquals(expected, actual)
    }

    @Test
    fun validateDecimalInputValidParamTest3() {
        val expected = true
        val actual = validators.validateDecimalInput(" 1 ")
        assertEquals(expected, actual)
    }
}