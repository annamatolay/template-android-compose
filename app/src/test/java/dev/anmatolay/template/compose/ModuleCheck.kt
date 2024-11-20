package dev.anmatolay.template.compose

import dev.anmatolay.template.compose.core.analytic.AnalyticsWrapper
import dev.anmatolay.template.compose.core.authentication.AuthenticatorWrapper
import dev.anmatolay.template.compose.core.di.*
import dev.anmatolay.template.compose.core.util.SharedPrefHandler
import dev.anmatolay.template.compose.core.util.UserProperty
import dev.anmatolay.template.compose.data.repository.UserCacheRepository
import dev.anmatolay.template.compose.data.source.local.UserIdDataSource
import dev.anmatolay.template.compose.domain.usecase.AuthenticationUseCase
import dev.anmatolay.template.compose.domain.usecase.MonitoringUseCase
import dev.anmatolay.template.compose.domain.usecase.UserIdUseCase
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.ksp.generated.module
import org.koin.test.verify.definition
import org.koin.test.verify.injectedParameters
import org.koin.test.verify.verify

class ModuleCheck: BaseTest() {

    @KoinExperimentalAPI
    @Test
    fun checkKoinModule() {
        CoreUtilModule().module.verify(
            injections = injectedParameters(
                definition<SharedPrefHandler>(android.content.Context::class)
            )
        )
        DataModule().module.verify(
            injections = injectedParameters(
                definition<UserIdDataSource>(SharedPrefHandler::class)
            )
        )
        DomainModule().module.verify(
            injections = injectedParameters(
                definition<AuthenticationUseCase>(AuthenticatorWrapper::class),
                definition<MonitoringUseCase>(AnalyticsWrapper::class, UserProperty::class),
                definition<UserIdUseCase>(UserCacheRepository::class)
            )
        )
        UIModule().module.verify()
    }
}
