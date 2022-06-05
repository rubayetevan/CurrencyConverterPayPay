package com.codesignal.paypay.currencyconverter.models

import androidx.room.*

@Entity(tableName = "LatestRates")
data class LatestRates(
    @PrimaryKey val timestamp: Int,
    @ColumnInfo var base: String? = null,
    @Embedded var rates: Rates? = Rates()
)