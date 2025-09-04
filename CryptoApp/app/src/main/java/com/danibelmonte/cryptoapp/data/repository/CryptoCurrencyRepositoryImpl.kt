package com.danibelmonte.cryptoapp.data.repository

import com.danibelmonte.cryptoapp.data.dataSource.remote.CryptoCurrencyDatasource
import com.danibelmonte.cryptoapp.data.dataSource.remote.dto.CryptoDto
import com.danibelmonte.cryptoapp.data.dataSource.remote.dto.CurrencyQuoteDto
import com.danibelmonte.cryptoapp.domain.entity.CryptoBo
import com.danibelmonte.cryptoapp.domain.entity.CurrencyQuoteBo
import com.danibelmonte.cryptoapp.domain.repository.CryptoCurrencyRepository
import javax.inject.Inject

class CryptoCurrencyRepositoryImpl @Inject constructor(val datasource: CryptoCurrencyDatasource): CryptoCurrencyRepository {
    override suspend fun getCryptoList(): List<CryptoBo> {
        return datasource.getCryptoList().map { it.toDomain() }
    }
}

fun CryptoDto.toDomain(): CryptoBo {
    return CryptoBo(
        id = id,
        name = name,
        symbol = symbol,
        slug = slug,
        cmcRank = cmcRank,
        circulatingSupply = circulatingSupply,
        totalSupply = totalSupply,
        maxSupply = maxSupply,
        infiniteSupply = infiniteSupply,
        lastUpdated = lastUpdated,
        usd = quote.usd.toDomain(),
        btc = quote.btc?.toDomain(),
        eth = quote.eth?.toDomain()
    )
}

fun CurrencyQuoteDto.toDomain() : CurrencyQuoteBo {
    return CurrencyQuoteBo(
        price = price,
        volume24h = volume24h,
        volumeChange24h = volumeChange24h,
        percentChange1h = percentChange1h,
        percentChange24h = percentChange24h,
        percentChange7d = percentChange7d,
        marketCap = marketCap,
        marketCapDominance = marketCapDominance,
        fullyDilutedMarketCap = fullyDilutedMarketCap,
        lastUpdated = lastUpdated
    )
}
