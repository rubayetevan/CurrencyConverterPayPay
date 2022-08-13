package com.codesignal.paypay.currencyconverter.repository

import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.models.CurrencyModel
import com.codesignal.paypay.currencyconverter.repository.local.LocalDataSource
import com.codesignal.paypay.currencyconverter.repository.remote.RemoteDataSource
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import java.util.*

class RepositoryTest {

    private val localDataSource: LocalDataSource = mock()
    private val remoteDataSource: RemoteDataSource = mock()
    private val externalScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private lateinit var repository: Repository

    @Before
    fun setUp() {
        repository = Repository(localDataSource, remoteDataSource, externalScope)
    }

    @Test
    fun getLatestRatesSuccessTest() {
        val bdt = CurrencyModel(
            name = "BDT",
            value = 95.019544
        )

        val usd = CurrencyModel(
            name = "USD",
            value = 1.0
        )

        val aed = CurrencyModel(
            name = "AED",
            value = 3.673
        )

        val currencyList = listOf<CurrencyModel>(bdt, usd, aed)

        localDataSource.stub {
            onBlocking { getAllCurrencyModel() }.thenReturn(currencyList)
        }

        runBlocking {
            val firstItem = repository.getLatestRates().first()
            assertTrue(firstItem is Resource.Loading)
            val secondItem = repository.getLatestRates().drop(1).first()
            assertTrue(secondItem is Resource.Success)
            assertFalse(secondItem.data.isNullOrEmpty())
            assertTrue(secondItem.data is List<CurrencyModel>)
            assertTrue(currencyList.size == secondItem.data?.size)
            assertEquals(currencyList, secondItem.data)
        }
    }

    @Test
    fun getLatestRatesEmptyTest() {

        val currencyList = emptyList<CurrencyModel>()

        localDataSource.stub {
            onBlocking { getAllCurrencyModel() }.thenReturn(currencyList)
        }

        runBlocking {
            val firstItem = repository.getLatestRates().first()
            assertTrue(firstItem is Resource.Loading)
            val secondItem = repository.getLatestRates().drop(1).first()
            assertTrue(secondItem is Resource.Empty)
        }
    }

    @Test
    fun getDBInitializedStatePositiveTest() {
        val expected = true
        Mockito.`when`(localDataSource.getDBInitializedState()).thenReturn(expected)
        val actual = repository.getDbInitializationState()
        assertEquals(expected, actual)
    }

    @Test
    fun getDBInitializedStateNegativeTest() {
        val expected = false
        Mockito.`when`(localDataSource.getDBInitializedState()).thenReturn(expected)
        val actual = repository.getDbInitializationState()
        assertEquals(expected, actual)
    }

    @Test
    fun updateOrInitializeDbSuccessTest(){
        val json =
            "{\n  \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n  \"license\": \"https://openexchangerates.org/license\",\n  \"timestamp\": 1660140000,\n  \"base\": \"USD\",\n  \"rates\": {\n    \"AED\": 3.673,\n    \"AFN\": 90.49998,\n    \"ALL\": 113.925,\n    \"AMD\": 406.392349,\n    \"ANG\": 1.803067,\n    \"AOA\": 429.5082,\n    \"ARS\": 134.0463,\n    \"AUD\": 1.415846,\n    \"AWG\": 1.8,\n    \"AZN\": 1.7,\n    \"BAM\": 1.910569,\n    \"BBD\": 2,\n    \"BDT\": 95.019544,\n    \"BGN\": 1.904763,\n    \"BHD\": 0.376955,\n    \"BIF\": 2043.5,\n    \"BMD\": 1,\n    \"BND\": 1.37804,\n    \"BOB\": 6.888249,\n    \"BRL\": 5.0663,\n    \"BSD\": 1,\n    \"BTC\": 0.000041584313,\n    \"BTN\": 79.508845,\n    \"BWP\": 12.537267,\n    \"BYN\": 2.525765,\n    \"BZD\": 2.016606,\n    \"CAD\": 1.28202,\n    \"CDF\": 2001,\n    \"CHF\": 0.941514,\n    \"CLF\": 0.032214,\n    \"CLP\": 888.96,\n    \"CNH\": 6.72339,\n    \"CNY\": 6.7312,\n    \"COP\": 4316.469296,\n    \"CRC\": 669.103272,\n    \"CUC\": 1,\n    \"CUP\": 25.75,\n    \"CVE\": 108.35,\n    \"CZK\": 23.615499,\n    \"DJF\": 178.55,\n    \"DKK\": 7.207,\n    \"DOP\": 54.25,\n    \"DZD\": 143.380667,\n    \"EGP\": 19.1385,\n    \"ERN\": 15,\n    \"ETB\": 52.2,\n    \"EUR\": 0.968722,\n    \"FJD\": 2.1811,\n    \"FKP\": 0.8179,\n    \"GBP\": 0.8179,\n    \"GEL\": 2.705,\n    \"GGP\": 0.8179,\n    \"GHS\": 8.879251,\n    \"GIP\": 0.8179,\n    \"GMD\": 54.2,\n    \"GNF\": 8800,\n    \"GTQ\": 7.738595,\n    \"GYD\": 209.309378,\n    \"HKD\": 7.847469,\n    \"HNL\": 24.549999,\n    \"HRK\": 7.2782,\n    \"HTG\": 121.207927,\n    \"HUF\": 383.523837,\n    \"IDR\": 14790.955878,\n    \"ILS\": 3.268597,\n    \"IMP\": 0.8179,\n    \"INR\": 79.145006,\n    \"IQD\": 1460,\n    \"IRR\": 42350,\n    \"ISK\": 135.54,\n    \"JEP\": 0.8179,\n    \"JMD\": 152.311217,\n    \"JOD\": 0.709,\n    \"JPY\": 132.5905,\n    \"KES\": 119.3,\n    \"KGS\": 82.612299,\n    \"KHR\": 4112,\n    \"KMF\": 481.850169,\n    \"KPW\": 900,\n    \"KRW\": 1299.984125,\n    \"KWD\": 0.3069,\n    \"KYD\": 0.83374,\n    \"KZT\": 478.470538,\n    \"LAK\": 15230,\n    \"LBP\": 1517.5,\n    \"LKR\": 360.601504,\n    \"LRD\": 153.499994,\n    \"LSL\": 16.6,\n    \"LYD\": 4.875,\n    \"MAD\": 10.265,\n    \"MDL\": 19.210529,\n    \"MGA\": 4185,\n    \"MKD\": 60.100618,\n    \"MMK\": 1864.202125,\n    \"MNT\": 3164.484996,\n    \"MOP\": 8.086744,\n    \"MRU\": 37.745,\n    \"MUR\": 45.652321,\n    \"MVR\": 15.38,\n    \"MWK\": 1017,\n    \"MXN\": 19.9975,\n    \"MYR\": 4.455,\n    \"MZN\": 63.899991,\n    \"NAD\": 16.6,\n    \"NGN\": 418.69,\n    \"NIO\": 35.9,\n    \"NOK\": 9.579096,\n    \"NPR\": 127.214012,\n    \"NZD\": 1.564987,\n    \"OMR\": 0.38494,\n    \"PAB\": 1,\n    \"PEN\": 3.9335,\n    \"PGK\": 3.525,\n    \"PHP\": 55.463508,\n    \"PKR\": 224.375,\n    \"PLN\": 4.538277,\n    \"PYG\": 6884.546719,\n    \"QAR\": 3.641,\n    \"RON\": 4.7563,\n    \"RSD\": 113.718333,\n    \"RUB\": 61.350007,\n    \"RWF\": 1029,\n    \"SAR\": 3.75985,\n    \"SBD\": 8.230542,\n    \"SCR\": 12.741687,\n    \"SDG\": 570.5,\n    \"SEK\": 10.070755,\n    \"SGD\": 1.37057,\n    \"SHP\": 0.8179,\n    \"SLL\": 13748.9,\n    \"SOS\": 581,\n    \"SRD\": 23.602,\n    \"SSP\": 130.26,\n    \"STD\": 22392.090504,\n    \"STN\": 24.15,\n    \"SVC\": 8.754518,\n    \"SYP\": 2512.53,\n    \"SZL\": 16.6,\n    \"THB\": 35.362,\n    \"TJS\": 10.234835,\n    \"TMT\": 3.51,\n    \"TND\": 3.1365,\n    \"TOP\": 2.3385,\n    \"TRY\": 17.9334,\n    \"TTD\": 6.799257,\n    \"TWD\": 29.9373,\n    \"TZS\": 2332.548,\n    \"UAH\": 36.952105,\n    \"UGX\": 3867.035789,\n    \"USD\": 1,\n    \"UYU\": 40.166868,\n    \"UZS\": 10895,\n    \"VES\": 5.84445,\n    \"VND\": 23390,\n    \"VUV\": 118.144513,\n    \"WST\": 2.705965,\n    \"XAF\": 635.440225,\n    \"XAG\": 0.04840271,\n    \"XAU\": 0.0005569,\n    \"XCD\": 2.70255,\n    \"XDR\": 0.734662,\n    \"XOF\": 635.440225,\n    \"XPD\": 0.00044702,\n    \"XPF\": 115.599329,\n    \"XPT\": 0.00106198,\n    \"YER\": 250.249937,\n    \"ZAR\": 16.2621,\n    \"ZMW\": 16.033313,\n    \"ZWL\": 322\n  }\n}"
        val jsonObject = JsonParser().parse(json).asJsonObject

        remoteDataSource.stub {
            onBlocking { getLatestRates() }.thenReturn(Resource.Success(data = jsonObject))
        }

        runBlocking {
            val first = repository.updateOrInitializeDB().first()
            assertTrue(first is Resource.Loading)
            val second = repository.updateOrInitializeDB().drop(1).first()
            assertTrue(second is Resource.Success)
            assertFalse(second.data.isNullOrEmpty())
            assertEquals(169,second.data?.size)
        }
    }

    @Test
    fun updateOrInitializeDbEmptyTest(){
        val json = "{}"
        val jsonObject = JsonParser().parse(json).asJsonObject

        remoteDataSource.stub {
            onBlocking { getLatestRates() }.thenReturn(Resource.Success(data = jsonObject))
        }

        runBlocking {
            val first = repository.updateOrInitializeDB().first()
            assertTrue(first is Resource.Loading)
            val second = repository.updateOrInitializeDB().drop(1).first()
            assertTrue(second is Resource.Empty)
        }
    }

    @Test
    fun getAllCurrencyNamesSuccessTest() {
        runBlocking {
            val expected = listOf("BDT", "USD", "AUD")
            Mockito.`when`(localDataSource.getAllCurrencyNames()).thenReturn(expected)
            val actual = repository.getAllCurrencyNames()
            val first = actual.first()
            assertTrue(first is Resource.Loading)
            val second = actual.drop(1).first()
            assertTrue(second is Resource.Success)
            assertFalse(second.data.isNullOrEmpty())
            assertTrue(second.data == expected)
        }
    }

    @Test
    fun getAllCurrencyNamesEmptyTest() {
        runBlocking {
            val expected = emptyList<String>()
            Mockito.`when`(localDataSource.getAllCurrencyNames()).thenReturn(expected)
            val actual = repository.getAllCurrencyNames()
            val first = actual.first()
            assertTrue(first is Resource.Loading)
            val second = actual.drop(1).first()
            assertTrue(second is Resource.Empty)
            assertTrue(second.data.isNullOrEmpty())
        }
    }

}