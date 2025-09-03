package com.danibelmonte.cryptoapp.domain.useCase

import com.danibelmonte.cryptoapp.domain.entity.CryptoBo
import com.danibelmonte.cryptoapp.domain.repository.CryptoCurrencyRepository
import javax.inject.Inject

class CryptoCurrencyUseCaseImpl @Inject constructor(val repository: CryptoCurrencyRepository): CryptoCurrencyUseCase {
    override suspend fun getCryptoList(): List<CryptoBo> {
        return repository.getCryptoList()
    }
}
