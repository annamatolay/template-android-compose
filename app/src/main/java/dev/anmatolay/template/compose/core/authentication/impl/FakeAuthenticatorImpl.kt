package dev.anmatolay.template.compose.core.authentication.impl

import dev.anmatolay.template.compose.core.authentication.AuthenticatorWrapper
import dev.anmatolay.template.compose.core.exception.UnknownAuthErrorException
import kotlinx.coroutines.flow.flow

class FakeAuthenticatorImpl : AuthenticatorWrapper {

    var isSuccessful: Boolean = false
    var userId = "null"

    override fun signInAnonymously() = flow {
        if (isSuccessful) {
            emit(Result.success(userId))
        } else {
            emit(
                Result.failure(
                    UnknownAuthErrorException(
                        isSuccessful = false,
                        isCurrentUserNull = true,
                    )
                )
            )
        }
    }
}
