package dev.anmatolay.template.compose.core.logging

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dev.anmatolay.template.compose.core.util.Constants.DEFAULT_USER_ID
import timber.log.Timber

// Only supposed to be used in production
class CrashlyticsLogTree(
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val userId: String?,
) : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority < Log.WARN) return

        firebaseCrashlytics.setUserId(userId ?: DEFAULT_USER_ID)

        if (firebaseCrashlytics.isCrashlyticsCollectionEnabled) {
            firebaseCrashlytics.log("$priority/$tag: $message")
            if (t != null) {
                firebaseCrashlytics.recordException(t)
            }
        }
        super.log(priority, tag, message, t)
    }
}
