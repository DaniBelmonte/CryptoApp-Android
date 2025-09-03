package com.danibelmonte.cryptoapp.di

import com.danibelmonte.cryptoapp.data.dataSource.remote.CryptoCurrencyDatasource
import com.danibelmonte.cryptoapp.data.repository.CryptoCurrencyRepositoryImpl
import com.danibelmonte.cryptoapp.domain.repository.CryptoCurrencyRepository
import com.danibelmonte.cryptoapp.domain.useCase.CryptoCurrencyUseCase
import com.danibelmonte.cryptoapp.domain.useCase.CryptoCurrencyUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun bindCryptoCurrencyRepository(
        datasource: CryptoCurrencyDatasource
    ): CryptoCurrencyRepository {
        return CryptoCurrencyRepositoryImpl(datasource)
    }
}
