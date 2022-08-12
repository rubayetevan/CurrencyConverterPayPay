package com.codesignal.paypay.currencyconverter.common.utility

class Validators {
    fun validateDecimalInput(s: String): Boolean {
        return s.toDoubleOrNull() != null
    }
}