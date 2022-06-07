package com.codesignal.paypay.currencyconverter.models

import kotlinx.coroutines.flow.StateFlow

data class CurrencyResult (val value :StateFlow<String>)