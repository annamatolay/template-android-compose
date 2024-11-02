package dev.anmatolay.template.compose.core.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("dev.anmatolay.localog.data")
class DataModule

@Module
@ComponentScan("dev.anmatolay.localog.domain")
class DomainModule

@Module
@ComponentScan("dev.anmatolay.localog.ui")
class UIModule
