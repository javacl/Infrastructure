package ir.javad.infrastructure.core.di

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomInterceptor @Inject constructor() : Interceptor {
    private var sessionToken: String? = null

    fun setSessionToken(sessionToken: String?) {
        this.sessionToken = sessionToken
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val requestBuilder = request.newBuilder()


        if (!sessionToken.isNullOrEmpty()) {
            requestBuilder.addHeader("token", sessionToken.toString())
        }


        return chain.proceed(requestBuilder.build())
    }
}