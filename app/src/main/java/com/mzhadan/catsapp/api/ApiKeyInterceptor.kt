package com.mzhadan.catsapp.api

import com.mzhadan.catsapp.entities.Constants.Companion.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        var request = original.newBuilder().addHeader("x-api-key", API_KEY).build()

        return chain.proceed(request)
    }
}