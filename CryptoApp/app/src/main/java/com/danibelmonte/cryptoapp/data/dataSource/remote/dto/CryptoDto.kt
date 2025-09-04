// CryptoDto.kt
package com.danibelmonte.cryptoapp.data.dataSource.remote.dto

import com.google.gson.annotations.SerializedName



data class ApiResponseDto(
    @SerializedName("data") val data: List<CryptoDto>,
)

// CryptoDto.kt (Gson)
data class CryptoDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("symbol") val symbol: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("cmc_rank") val cmcRank: Int? = null,

    @SerializedName("num_market_pairs") val numMarketPairs: Int? = null,

    @SerializedName("circulating_supply") val circulatingSupply: Double? = null,
    @SerializedName("total_supply") val totalSupply: Double? = null,
    @SerializedName("max_supply") val maxSupply: Double? = null,

    @SerializedName("infinite_supply") val infiniteSupply: Boolean? = null,
    @SerializedName("last_updated") val lastUpdated: String? = null,
    @SerializedName("date_added") val dateAdded: String? = null,
    @SerializedName("tags") val tags: List<String> = emptyList(),
    @SerializedName("platform") val platform: Any? = null,

    @SerializedName("self_reported_circulating_supply") val selfReportedCirculatingSupply: Double? = null,
    @SerializedName("self_reported_market_cap") val selfReportedMarketCap: Double? = null,

    @SerializedName("quote") val quote: QuoteDto
)
