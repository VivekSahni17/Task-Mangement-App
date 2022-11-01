package com.techarion.techarion.authentication

import android.util.Log
import com.techarion.techarion.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor():Interceptor {
    @Inject
    lateinit var tokenManager: TokenManager
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.addHeader("Accept", "Application/JSON")
        request.addHeader("Authorization", "Bearer " + (tokenManager.getToken()))
        //Log.d("vivek",tokenManager.getToken().toString())
        return chain.proceed(request.build())
    }
}