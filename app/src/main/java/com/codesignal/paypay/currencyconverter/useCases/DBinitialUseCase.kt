package com.codesignal.paypay.currencyconverter.useCases

import com.codesignal.paypay.currencyconverter.repository.Repository
import javax.inject.Inject

class DBinitialUseCase @Inject constructor(private val repository: Repository) {

    fun shouldUpdateDB(): Boolean = repository.shouldUpdateDB()

    fun getDbUpdateTime() = repository.getDbUpdateTime()

    fun getDbInitializationState() = repository.getDbInitializationState()
}