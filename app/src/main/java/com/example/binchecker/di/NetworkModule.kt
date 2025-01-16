package com.example.binchecker.di

import com.example.binchecker.data.Constant.BASE_URL
import com.example.binchecker.data.network.ApiService
import com.example.binchecker.repository.BINNetworkRepository
import com.example.binchecker.repository.BINNetworkRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideBINRepository(apiService: ApiService): BINNetworkRepository {
        return BINNetworkRepositoryImpl(apiService)
    }

}