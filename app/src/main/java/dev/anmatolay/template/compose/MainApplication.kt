package dev.anmatolay.template.compose

import android.app.Application
import dev.anmatolay.template.compose.core.di.KoinInitializer
import dev.anmatolay.template.compose.domain.usecase.AuthenticationUseCase
import dev.anmatolay.template.compose.domain.usecase.MonitoringUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainApplication : Application() {

    private val monitoringUseCase by inject<MonitoringUseCase>()
    private val authenticationUseCase by inject<AuthenticationUseCase>()

    override fun onCreate() {
        super.onCreate()

        KoinInitializer.init(this)

        monitoringUseCase.isCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        // Calling first on flow ensures that the flow collection ends
        // after the first emission, which will cancel the coroutine automatically
        // (causing no issue that this scope not tied to the lifecycle of thw application
        CoroutineScope(Dispatchers.Main + SupervisorJob())
            .launch {
                try {
                    val result = authenticationUseCase.signIn().first()
                    when {
                        result.isSuccess ->
                            Timber.i("Authentication: successful!")

                        result.isFailure ->
                            Timber.e("Authentication: unsuccessful!")
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Authentication error:")
                }
            }
    }
}
