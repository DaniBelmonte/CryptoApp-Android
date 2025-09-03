package com.danibelmonte.cryptoapp.data.repository

import com.danibelmonte.cryptoapp.data.dataSource.remote.CryptoCurrencyDatasource
import com.danibelmonte.cryptoapp.data.dataSource.remote.dto.CryptoDto
import com.danibelmonte.cryptoapp.domain.entity.CryptoBo
import com.danibelmonte.cryptoapp.domain.repository.CryptoCurrencyRepository
import javax.inject.Inject

class CryptoCurrencyRepositoryImpl @Inject constructor(val datasource: CryptoCurrencyDatasource): CryptoCurrencyRepository {
    override suspend fun getCryptoList(): List<CryptoBo> {
        return datasource.getCryptoList().map { it.toDomain() }
    }
}

fun CryptoDto.toDomain(): CryptoBo {
    return CryptoBo(
        id = this.id,
        name = this.name,
        symbol = this.symbol,
        slug = this.slug,
        cmcRank = this.cmcRank,
        numMarketPairs = this.numMarketPairs,
        circulatingSupply = this.circulatingSupply,
        totalSupply = this.totalSupply,
        maxSupply = this.maxSupply,
        lastUpdated = this.lastUpdated,
        dateAdded = this.dateAdded,
        tags = this.tags,
        platform = this.platform?.toString(),
        price = this.quote.usd.price,
        volume24h = this.quote.usd.volume24h,
        percentChange1h = this.quote.usd.percentChange1h,
        percentChange24h = this.quote.usd.percentChange24h,
        percentChange7d = this.quote.usd.percentChange7d,
        marketCap = this.quote.usd.marketCap,
        btcPrice = this.quote.btc?.price ?: 0.0
    )
}
