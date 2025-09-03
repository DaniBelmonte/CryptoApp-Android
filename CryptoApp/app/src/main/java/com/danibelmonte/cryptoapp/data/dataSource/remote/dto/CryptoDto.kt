package com.danibelmonte.cryptoapp.data.dataSource.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class CryptoDto(
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    @SerialName("cmc_rank") val cmcRank: Int,
    @SerialName("num_market_pairs") val numMarketPairs: Int,
    @SerialName("circulating_supply") val circulatingSupply: Long,
    @SerialName("total_supply") val totalSupply: Long,
    @SerialName("max_supply") val maxSupply: Long?,
    @SerialName("last_updated") val lastUpdated: String,
    @SerialName("date_added") val dateAdded: String,
    val tags: List<String>,
    val platform: Any?,
    val quote: QuoteDto
)
