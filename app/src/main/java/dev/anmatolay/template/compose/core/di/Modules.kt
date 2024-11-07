package dev.anmatolay.template.compose.core.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("dev.anmatolay.template.compose.data")
class DataModule

@Module
@ComponentScan("dev.anmatolay.template.compose.domain")
class DomainModule

@Module
@ComponentScan("dev.anmatolay.template.compose.ui")
class UIModule

@Module
@ComponentScan("dev.anmatolay.template.compose.core.util")
class CoreUtilModule

// Only for prod.
@Module
@ComponentScan("dev.anmatolay.template.compose.core.firebase")
class FirebaseModule

// Only for testing
@Module
@ComponentScan("dev.anmatolay.template.compose.core.authentication.impl")
class FakeAuthenticationModule

// Only for testing
@Module
@ComponentScan("dev.anmatolay.template.compose.core.analytic.impl")
class FakeAnalyticsModule
