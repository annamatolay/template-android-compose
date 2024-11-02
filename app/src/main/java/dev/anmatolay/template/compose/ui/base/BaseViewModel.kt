package dev.anmatolay.template.compose.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Represent the current state of the UI by the VM (and data) which based on the UI render itself
 */
sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val error: Throwable) : UiState<Nothing>()
}

/**
 * Represents user actions (like button clicks or text input)
 */
interface UiIntent

/**
 * Represent single UI effect without state change (like showing a toast)
 */
interface UiSideEffect

/**
 * Ensuring simplified MVI architecture (no action, render etc) with generic error handling and utility
 */
abstract class BaseViewModel<State : UiState<*>, Intent : UiIntent, SideEffect : UiSideEffect> : ViewModel() {

    // Reactive updates in the UI.
    private val _stateFlow = MutableStateFlow<UiState<State>>(UiState.Loading)
    val state: StateFlow<UiState<State>> = _stateFlow

    // Supports handling multiple emissions without losing any.
    private val _intentFlow = MutableSharedFlow<Intent>()

    // For one-time, single-consumer events.
    private val _sideEffectChannel = Channel<SideEffect>()
    val sideEffect = _sideEffectChannel.receiveAsFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    /**
     * Handle each intent in a single coroutine
     */
    init {
        viewModelScope.launch {
            _intentFlow.collect { intent ->
                processIntent(intent)
            }
        }
    }

    /**
     * Ensure VM handling all the relevant user intents
     */
    abstract fun processIntent(intent: Intent)

    /**
     * To be called from VM to interact with lower layers.
     */
    fun launchViewModelScope(
        context: CoroutineContext = exceptionHandler,
        block: suspend CoroutineScope.() -> Unit,
    ) = viewModelScope.launch(context = context, block = block)

    /**
     * To be called from UI to send user intents to the ViewModel.
     */
    fun sendIntent(intent: Intent) {
        viewModelScope.launch { _intentFlow.emit(intent) }
    }

    /**
     * To be called from UI to trigger side effect.
     */
    fun triggerSideEffect(effect: SideEffect) {
        viewModelScope.launch {
            _sideEffectChannel.send(effect)
        }
    }

    /**
     * To be called from UI to handle side effect to the ViewModel.
     */
    @Composable
    fun handleSideEffects(onSideEffectTrigger: (SideEffect) -> Unit) {
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            scope.launch {
                sideEffect.collect { effect ->
                    onSideEffectTrigger(effect)
                }
            }
        }
    }

    /**
     * Generic error handling
     */
    private fun onError(throwable: Throwable) {
        _stateFlow.value = UiState.Error(throwable)
        Timber.e(throwable)
    }
}
