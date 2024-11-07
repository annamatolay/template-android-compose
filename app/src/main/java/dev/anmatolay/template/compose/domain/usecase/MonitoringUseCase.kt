package dev.anmatolay.template.compose.domain.usecase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import dev.anmatolay.template.compose.BuildConfig
import dev.anmatolay.template.compose.core.analytic.AnalyticsWrapper
import dev.anmatolay.template.compose.core.logging.CrashlyticsLogTree
import dev.anmatolay.template.compose.core.logging.DiamondDebugTree
import dev.anmatolay.template.compose.core.util.UserProperty
import dev.anmatolay.template.compose.core.util.UserProperty.Companion.KEY_ANDROID_VERSION
import dev.anmatolay.template.compose.core.util.UserProperty.Companion.KEY_API_LEVEL
import dev.anmatolay.template.compose.core.util.UserProperty.Companion.KEY_APP_VERSION
import org.koin.core.annotation.Factory
import timber.log.Timber

@Factory
class MonitoringUseCase(
    private val analyticsWrapper: AnalyticsWrapper,
    private val userProperty: UserProperty
) {
    private val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    fun setUserProperties() {
        analyticsWrapper.setUserProperty(KEY_APP_VERSION, userProperty.version)
        analyticsWrapper.setUserProperty(KEY_ANDROID_VERSION, userProperty.osVersion)
        analyticsWrapper.setUserProperty(KEY_API_LEVEL, userProperty.sdkVersion.toString())
        Timber.i("User properties set!")
        Timber.d(userProperty.toString())
    }

    fun setUpAnalyticsAndLogging(userId: String?) {
        analyticsWrapper.setUserId(userId)
        Timber.plant(
            if (BuildConfig.DEBUG)
                DiamondDebugTree()
            else
                CrashlyticsLogTree(crashlytics, userId)
        )
    }

    fun isCrashlyticsCollectionEnabled(isEnabled: Boolean) {
        crashlytics.isCrashlyticsCollectionEnabled = isEnabled

    }
}
