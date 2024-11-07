package dev.anmatolay.template.compose.domain.usecase

import dev.anmatolay.template.compose.core.authentication.AuthenticatorWrapper
import dev.anmatolay.template.compose.core.exception.UnknownAuthErrorException
import dev.anmatolay.template.compose.core.util.Constants.DEFAULT_USER_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Factory
import timber.log.Timber

@Factory
class AuthenticationUseCase(
    private val userIdUseCase: UserIdUseCase,
    private val authenticatorWrapper: AuthenticatorWrapper,
    private val monitoringUseCase: MonitoringUseCase,
) {

    /**
     * This method checks for a stored user ID in shared preferences.
     * If the ID is default, it proceeds with authentication.
     * If an ID exists, it sets up monitoring without authentication.
     * This method should be called only once during the app lifecycle (onCreate).
     * It sets up Firebase Analytics and Crashlytics with the Firebase Anonymous ID
     * (or falls back to the default ID).
     */
    fun signIn(): Flow<Result<Unit>> = flow {
        val cachedOrDefaultId = userIdUseCase.retrieve().first()

        if (cachedOrDefaultId == DEFAULT_USER_ID) {
            val authResult = authenticatorWrapper.signInAnonymously().first()

            if (authResult.isSuccess) {
                val newUserId = authResult.getOrThrow()
                userIdUseCase.cache(newUserId).first()
                monitoringUseCase.setUpAnalyticsAndLogging(newUserId)
                monitoringUseCase.setUserProperties()
                Timber.d("Monitoring up and ready for new user!")
                emit(Result.success(Unit))
            } else {
                monitoringUseCase.setUpAnalyticsAndLogging(DEFAULT_USER_ID)
                Timber.w("Monitoring up and ready for unknown user!")
                emit(Result.failure(authResult.createExceptionFromAuthResult()))
            }
        } else {
            monitoringUseCase.setUpAnalyticsAndLogging(cachedOrDefaultId)
            Timber.d("Monitoring up and ready for existing user!")
            emit(Result.success(Unit))
        }
    }

    // Covering edge case when Result doesn't have an exception
    private fun Result<String>.createExceptionFromAuthResult() =
        this.exceptionOrNull() ?: UnknownAuthErrorException(
            isSuccessful = this.isSuccess,
            isCurrentUserNull = this.getOrNull().isNullOrEmpty()
        )
}
