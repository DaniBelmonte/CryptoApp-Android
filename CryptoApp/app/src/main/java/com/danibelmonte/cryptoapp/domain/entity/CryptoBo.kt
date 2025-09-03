package com.danibelmonte.cryptoapp.domain.entity

data class CryptoBo(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    val cmcRank: Int,
    val numMarketPairs: Int,
    val circulatingSupply: Long,
    val totalSupply: Long,
    val maxSupply: Long?,
    val lastUpdated: String,
    val dateAdded: String,
    val tags: List<String>,
    val platform: String?,
    val price: Double,
    val volume24h: Double,
    val percentChange1h: Double,
    val percentChange24h: Double,
    val percentChange7d: Double,
    val marketCap: Double,
    val btcPrice: Double
)
