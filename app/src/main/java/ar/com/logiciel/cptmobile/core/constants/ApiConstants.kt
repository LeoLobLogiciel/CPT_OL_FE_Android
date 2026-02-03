package ar.com.logiciel.cptmobile.core.constants

object ApiConstants {
    // Base URL for emulator - 10.0.2.2 maps to localhost on the host machine
    const val BASE_URL = "http://10.0.2.2:8080/api/"

    // For physical device, use your computer's local IP address
    // const val BASE_URL = "http://192.168.1.XXX:8080/api/"

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
