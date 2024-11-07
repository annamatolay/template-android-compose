package dev.anmatolay.template.compose.core.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.anmatolay.template.compose.core.authentication.AuthenticatorWrapper
import dev.anmatolay.template.compose.core.exception.UnknownAuthErrorException
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single
import timber.log.Timber

@Single
class FirebaseAuthenticatorImpl : AuthenticatorWrapper {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun signInAnonymously() = callbackFlow {
        try {
            val task = firebaseAuth.signInAnonymously()
            task.await()
            val currentUser = firebaseAuth.currentUser
            if (task.isSuccessful && currentUser != null) {
                val id = currentUser.uid
                Timber.tag(TAG).i("User anonymously signed in with ID: $id")
                trySend(Result.success(id))
            } else {
                val exception = task.exception ?: UnknownAuthErrorException(task.isSuccessful, currentUser.isNull())
                handleError(exception)
            }
        } catch (e: Exception) {
            // Respond immediately to errors specific to the logic of the flow
            handleError(e)
        }

        awaitClose { /* Clean up resources if needed */ }
    }.catch { cause ->
        // This is a fallback mechanism for unexpected errors or for errors that occur outside the specific flow logic.
        Timber.tag(TAG).e(cause)
        emit(Result.failure(cause))
    }

    private fun ProducerScope<Result<String>>.handleError(exception: java.lang.Exception) {
        Timber.tag(TAG).e(exception)
        trySend(Result.failure(exception))
    }


    companion object {
        private const val TAG = "FirebaseAuthenticator"
    }
}

private fun FirebaseUser?.isNull(): Boolean = this == null
