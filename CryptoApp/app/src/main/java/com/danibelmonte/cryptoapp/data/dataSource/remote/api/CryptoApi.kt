package com.danibelmonte.cryptoapp.data.dataSource.remote.api
import com.danibelmonte.cryptoapp.data.dataSource.remote.dto.ApiResponseDto
import com.danibelmonte.cryptoapp.data.dataSource.remote.dto.CryptoDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CryptoApi {
    @GET("/v1/cryptocurrency/listings/latest")
    suspend fun getCryptoList(): ApiResponseDto
}

