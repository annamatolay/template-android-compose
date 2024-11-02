package dev.anmatolay.template.compose

import android.app.Application
import dev.anmatolay.template.compose.core.di.KoinInitializer
import dev.anmatolay.template.compose.core.logging.DiamondDebugTree
import dev.anmatolay.template.compose.core.logging.FakeCrashReportTree
import timber.log.Timber

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        KoinInitializer.init(this)

        Timber.plant(if (BuildConfig.DEBUG) DiamondDebugTree() else FakeCrashReportTree())
        Timber.i("Application CREATED")
    }
}
