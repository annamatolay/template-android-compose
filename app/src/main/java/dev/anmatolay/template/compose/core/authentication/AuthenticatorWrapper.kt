package dev.anmatolay.template.compose.core.authentication

import kotlinx.coroutines.flow.Flow

interface AuthenticatorWrapper {
    fun signInAnonymously(): Flow<Result<String>>
}
