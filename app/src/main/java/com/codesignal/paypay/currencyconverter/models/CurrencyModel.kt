package com.codesignal.paypay.currencyconverter.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Currencies", indices = [Index(value = ["name"], unique = true)])
data class CurrencyModel(
    @ColumnInfo val name: String,
    @ColumnInfo val value: Double,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
