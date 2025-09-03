package com.danibelmonte.cryptoapp.domain.repository

import com.danibelmonte.cryptoapp.domain.entity.CryptoBo

interface CryptoCurrencyRepository {
    suspend fun getCryptoList(): List<CryptoBo>
}
