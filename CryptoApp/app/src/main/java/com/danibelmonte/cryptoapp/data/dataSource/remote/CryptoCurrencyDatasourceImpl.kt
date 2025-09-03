package com.danibelmonte.cryptoapp.data.dataSource.remote

import com.danibelmonte.cryptoapp.data.dataSource.remote.api.CryptoApi
import com.danibelmonte.cryptoapp.data.dataSource.remote.dto.CryptoDto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoCurrencyDatasourceImpl: CryptoCurrencyDatasource {

    private val httpLoginInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(httpLoginInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pro-api.coinmarketcap.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val api = retrofit.create(CryptoApi::class.java)

    override suspend fun getCryptoList(): List<CryptoDto> {
        return api.getCryptoList()
    }
}
