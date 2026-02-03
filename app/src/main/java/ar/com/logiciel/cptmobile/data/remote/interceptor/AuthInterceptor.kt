package ar.com.logiciel.cptmobile.data.remote.interceptor

import ar.com.logiciel.cptmobile.core.constants.ApiConstants
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        Timber.d("Adding auth headers to request: ${originalRequest.url}")

        val modifiedRequest = originalRequest.newBuilder()
            .addHeader(ApiConstants.HEADER_LSI_PASS, ApiConstants.DEFAULT_LSI_PASS)
            .addHeader(ApiConstants.HEADER_LSI_TOKEN, ApiConstants.DEFAULT_LSI_TOKEN)
            .build()

        return chain.proceed(modifiedRequest)
    }
}
