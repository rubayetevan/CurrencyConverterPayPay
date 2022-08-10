package com.codesignal.paypay.currencyconverter.repository.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.codesignal.paypay.currencyconverter.models.CurrencyModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest{
    private lateinit var currencyModelDao: CurrencyModelDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        currencyModelDao = db.currencyModelDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val currencyModel = CurrencyModel(
            name = "AED",
            value = 3.673
        )
        val currencyModels: MutableList<CurrencyModel> = ArrayList<CurrencyModel>()
        currencyModels.add(currencyModel)

        runBlocking {
            currencyModelDao.insertAll(currencyModels)
            val allCurrencies = currencyModelDao.getAllCurrencies()
            assertEquals(currencyModels,allCurrencies)

            val currencyNames = currencyModelDao.getAllCurrencyNames()
            assertTrue(currencyNames.size==1)
            assertEquals(currencyModel.name, currencyNames[0])
        }

    }

}