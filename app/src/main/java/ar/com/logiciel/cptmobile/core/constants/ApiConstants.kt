package ar.com.logiciel.cptmobile.core.constants

import ar.com.logiciel.cptmobile.BuildConfig

object ApiConstants {
    // Base URL configurada según el build type (debug o release)
    // Debug: http://10.0.2.2:8080/api/ (emulador apunta a localhost)
    // Release: https://logiciel.cptoficina.com.ar:8123/api/ (servidor producción)
    val BASE_URL = BuildConfig.API_BASE_URL

    // Authentication headers
    const val HEADER_LSI_PASS = "LSIPass"
    const val HEADER_LSI_TOKEN = "LSIToken"

    // Default values (matching the API requirements)
    const val DEFAULT_LSI_PASS = "CPT!"
    const val DEFAULT_LSI_TOKEN = "LSIToken1789"

    // Timeout values
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L

    // DataStore keys
    const val DATASTORE_NAME = "cpt_preferences"
    const val KEY_USER_TOKEN = "user_token"
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_EMAIL = "user_email"
    const val KEY_USER_NAME = "user_name"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
}
