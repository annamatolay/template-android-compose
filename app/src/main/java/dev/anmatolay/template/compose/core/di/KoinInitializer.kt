package dev.anmatolay.template.compose.core.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

object KoinInitializer {

    fun init(application: Application) {
        startKoin {
            androidLogger()
            androidContext(application)
            modules(
                DataModule().module,
                DomainModule().module,
                UIModule().module,
            )
        }
    }
}
