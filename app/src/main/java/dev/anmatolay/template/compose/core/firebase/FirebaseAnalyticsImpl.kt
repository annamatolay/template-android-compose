package dev.anmatolay.template.compose.core.firebase

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dev.anmatolay.template.compose.core.analytic.AnalyticsWrapper
import org.koin.core.annotation.Single

@Single
class FirebaseAnalyticsImpl(context: Context) : AnalyticsWrapper {
    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
    }

    override fun setUserProperty(name: String, value: String) {
        firebaseAnalytics.setUserProperty(name, value)
    }


    override fun logEven(name: String, bundle: Bundle) =
        firebaseAnalytics.logEvent(name, bundle)
}
