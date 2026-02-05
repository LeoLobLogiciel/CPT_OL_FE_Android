package ar.com.logiciel.cptmobile.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Interceptor to validate that large API responses are not truncated.
 * This is especially important for endpoints that return many records.
 */
class ResponseValidationInterceptor @Inject constructor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // Only validate JSON responses
        val contentType = response.body?.contentType()
        if (contentType?.subtype != "json") {
            return response
        }

        // Read the response body
        val source = response.body?.source()
        source?.request(Long.MAX_VALUE) // Request the entire body
        val buffer = source?.buffer

        // Clone the buffer to read without consuming
        val bodyString = buffer?.clone()?.readUtf8() ?: ""
        val bodySize = bodyString.length

        // Log response size
        val url = request.url.toString()
        Timber.d("📊 Response from $url: $bodySize bytes")

        // Check if response is large (> 1MB)
        if (bodySize > 1_000_000) {
            Timber.w("⚠️ Large response detected: ${bodySize / 1_000_000.0} MB from $url")
        }

        // CRITICAL: Validate JSON is complete
        val trimmedBody = bodyString.trim()
        val lastChar = trimmedBody.lastOrNull()

        if (lastChar != '}' && lastChar != ']') {
            Timber.e("❌❌❌ TRUNCATED RESPONSE DETECTED!")
            Timber.e("   URL: $url")
            Timber.e("   Size: $bodySize bytes")
            Timber.e("   Expected ending: } or ]")
            Timber.e("   Actual last char: '$lastChar'")
            Timber.e("   Last 200 chars:")
            Timber.e(trimmedBody.takeLast(200))

            // Optionally throw an exception to force retry
            // throw IOException("Response appears to be truncated")
        }

        // Check for incomplete JSON patterns that indicate truncation
        if (trimmedBody.contains("\"EsPrimero\":false,\"B") && !trimmedBody.endsWith("}")) {
            Timber.e("❌❌❌ KNOWN TRUNCATION PATTERN DETECTED!")
            Timber.e("   Response ends with incomplete field starting with 'B'")
            Timber.e("   This is a clear sign of truncated JSON")
        }

        // Create new response body with the same content
        val newBody = bodyString.toResponseBody(contentType)

        return response.newBuilder()
            .body(newBody)
            .build()
    }
}
