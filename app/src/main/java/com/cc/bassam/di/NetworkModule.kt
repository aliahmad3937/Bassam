package com.cc.bassam.di


import com.cc.bassam.retrofit.ApiService
import com.cc.bassam.utils.Constants.BASE_URL
import com.cc.bassam.utils.Constants.RETRO_TIMEOUT
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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


    @JvmStatic
    @Provides
    @Singleton
    fun provideApiInterface(okHttpClient: OkHttpClient, gson: Gson): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }


    @get:Provides
    val okHttpClient: OkHttpClient
        get() {
            val okHttpClientBuilder = OkHttpClient.Builder()
            okHttpClientBuilder.readTimeout(RETRO_TIMEOUT, TimeUnit.SECONDS);
            okHttpClientBuilder.connectTimeout(RETRO_TIMEOUT, TimeUnit.SECONDS);
            okHttpClientBuilder.writeTimeout(RETRO_TIMEOUT, TimeUnit.SECONDS);

            return okHttpClientBuilder.build()
        }


    @get:Provides
    val gson: Gson
        get() {
            return GsonBuilder()
                .setLenient()
                .create()
        }
}