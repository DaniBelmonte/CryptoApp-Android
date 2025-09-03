package com.danibelmonte.cryptoapp.domain.useCase

import com.danibelmonte.cryptoapp.domain.entity.CryptoBo

interface CryptoCurrencyUseCase {
    suspend fun getCryptoList(): List<CryptoBo>
}
