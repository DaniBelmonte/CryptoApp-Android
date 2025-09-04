// CryptoBo.kt
package com.danibelmonte.cryptoapp.domain.entity

data class CryptoBo(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    val cmcRank: Int?,
    val circulatingSupply: Double?,
    val totalSupply: Double?,
    val maxSupply: Double?,
    val infiniteSupply: Boolean?,
    val lastUpdated: String?,
    val usd: CurrencyQuoteBo,
    val btc: CurrencyQuoteBo?,
    val eth: CurrencyQuoteBo?
)

data class CurrencyQuoteBo(
    val price: Double,
    val volume24h: Double,
    val volumeChange24h: Double?,
    val percentChange1h: Double,
    val percentChange24h: Double,
    val percentChange7d: Double,
    val marketCap: Double,
    val marketCapDominance: Double?,
    val fullyDilutedMarketCap: Double?,
    val lastUpdated: String
)
