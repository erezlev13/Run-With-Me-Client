package com.runwithme.runwithme.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {

    var tokenStoreImpl : TokenStoreImpl = TokenStoreImpl()
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest: Request = chain.request()
            .newBuilder().addHeader("authorization", "Bearer " + tokenStoreImpl.token).build()
        return chain.proceed(newRequest)
    }
}