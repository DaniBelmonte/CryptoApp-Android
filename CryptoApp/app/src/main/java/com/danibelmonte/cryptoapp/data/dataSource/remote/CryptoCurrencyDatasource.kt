package com.danibelmonte.cryptoapp.data.dataSource.remote

import com.danibelmonte.cryptoapp.data.dataSource.remote.dto.CryptoDto

interface CryptoCurrencyDatasource {
    suspend fun getCryptoList(): List<CryptoDto>
}
