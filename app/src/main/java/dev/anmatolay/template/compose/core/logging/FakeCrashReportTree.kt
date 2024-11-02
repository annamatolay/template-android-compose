package dev.anmatolay.template.compose.core.logging

import timber.log.Timber

class FakeCrashReportTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // Avoid logging with release build until crash reporting not set up
        // If you would like to see how I would do it you can check here:
        // Github - annamatolay/template-android-xml - core/logging/CrashlyticsLogTree.kt
        // https://urlr.me/2DWz8
    }
}
