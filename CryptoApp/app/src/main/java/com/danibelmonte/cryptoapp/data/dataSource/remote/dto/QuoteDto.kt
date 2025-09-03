package com.danibelmonte.cryptoapp.data.dataSource.remote.dto

import com.google.gson.annotations.SerializedName

data class QuoteDto(
    @SerializedName("USD") val usd: CurrencyQuoteDto,
    @SerializedName("BTC") val btc: CurrencyQuoteDto? = null
)

data class CurrencyQuoteDto(
    val price: Double,
    @SerializedName("volume_24h") val volume24h: Double,
    @SerializedName("percent_change_1h") val percentChange1h: Double,
    @SerializedName("percent_change_24h") val percentChange24h: Double,
    @SerializedName("percent_change_7d") val percentChange7d: Double,
    @SerializedName("market_cap") val marketCap: Double,
    @SerializedName("last_updated") val lastUpdated: String
)
