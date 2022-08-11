package com.codesignal.paypay.currencyconverter.useCases

import com.codesignal.paypay.currencyconverter.common.utility.DB_UPDATE_TH_MIN
import com.codesignal.paypay.currencyconverter.repository.Repository
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
}