package com.mzhadan.catsapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mzhadan.catsapp.api.ApiKeyInterceptor
import com.mzhadan.catsapp.api.CatsApiService
import com.mzhadan.catsapp.entities.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class CatsApiServiceModule {

    @Singleton
    @Provides
    fun provideGson(): Gson =
        GsonBuilder().create()


    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(ApiKeyInterceptor())
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideCatsApiService(retrofit: Retrofit): CatsApiService =
        retrofit.create(CatsApiService::class.java)
}