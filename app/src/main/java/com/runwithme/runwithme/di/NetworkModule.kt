package com.runwithme.runwithme.di


import com.runwithme.runwithme.network.AuthInterceptor
import com.runwithme.runwithme.network.RunWithMeService
import com.runwithme.runwithme.utils.Constants.BASE_URL
import com.runwithme.runwithme.network.TokenStoreImpl
import com.runwithme.runwithme.network.maps.MapsService
import com.runwithme.runwithme.utils.Constants.MAPS_STATIC_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideTokenStore(tokenStore: TokenStoreImpl): TokenStoreImpl {
        return tokenStore
    }

    @Singleton
    @Provides
    fun provideHttpClient(authInterceptor: AuthInterceptor) : OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): RunWithMeService {
        return retrofit.create(RunWithMeService::class.java)
    }
}