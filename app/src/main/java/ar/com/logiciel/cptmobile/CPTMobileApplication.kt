package ar.com.logiciel.cptmobile

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CPTMobileApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    // Add custom formatting to make logs easier to read
                    super.log(priority, "CPT-$tag", message, t)
                }
            })

            Timber.d("CPT Mobile App initialized")
            Timber.d("API Base URL: ${ar.com.logiciel.cptmobile.core.constants.ApiConstants.BASE_URL}")
        }
    }
}
