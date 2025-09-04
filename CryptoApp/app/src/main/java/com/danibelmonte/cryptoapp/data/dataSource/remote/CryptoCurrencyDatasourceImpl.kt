package com.danibelmonte.cryptoapp.data.dataSource.remote

import com.danibelmonte.cryptoapp.data.dataSource.remote.api.CryptoApi
import com.danibelmonte.cryptoapp.data.dataSource.remote.dto.CryptoDto
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.danibelmonte.cryptoapp.BuildConfig
import okhttp3.Interceptor

class CryptoCurrencyDatasourceImpl: CryptoCurrencyDatasource {

    private val apiKeyInterceptor = Interceptor { chain ->
        val req = chain.request().newBuilder()
            .addHeader("X-CMC_PRO_API_KEY", "5ca2b5be-08de-4565-8b1e-52890bca811e")
            .addHeader("Accept", "application/json")
            .build()
        chain.proceed(req)
    }

    private val httpLoginInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(httpLoginInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pro-api.coinmarketcap.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val api = retrofit.create(CryptoApi::class.java)

    override suspend fun getCryptoList(): List<CryptoDto> {
        return api.getCryptoList().data
    }
}
