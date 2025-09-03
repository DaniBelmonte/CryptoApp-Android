package com.danibelmonte.cryptoapp.di

import com.danibelmonte.cryptoapp.data.dataSource.remote.CryptoCurrencyDatasource
import com.danibelmonte.cryptoapp.data.dataSource.remote.CryptoCurrencyDatasourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatasourceModule {
    @Provides
    fun provideCryptoCurrencyDatasource():
        CryptoCurrencyDatasource {
        return CryptoCurrencyDatasourceImpl()
    }
}
