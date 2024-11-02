package dev.anmatolay.template.compose

import dev.anmatolay.template.compose.core.di.DataModule
import dev.anmatolay.template.compose.core.di.DomainModule
import dev.anmatolay.template.compose.core.di.UIModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.ksp.generated.module
import org.koin.test.verify.verify

class ModuleCheck {

    @KoinExperimentalAPI
    @Test
    fun checkKoinModule() {
        DataModule().module.verify()
        DomainModule().module.verify()
        UIModule().module.verify()
    }
}
