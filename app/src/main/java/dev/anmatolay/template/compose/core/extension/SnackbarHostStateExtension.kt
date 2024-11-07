package dev.anmatolay.template.compose.core.extension

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult


suspend fun SnackbarHostState.createAndShowSnackbar(
    message: String,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onActionPerformed: () -> Unit = {},
) {
    this.showSnackbar(message, actionLabel, withDismissAction, duration)
        .apply {
            when (this) {
                SnackbarResult.ActionPerformed -> {
                    onActionPerformed()
                }

                SnackbarResult.Dismissed -> {
                    this@createAndShowSnackbar.currentSnackbarData?.dismiss()
                }
            }
        }
}
