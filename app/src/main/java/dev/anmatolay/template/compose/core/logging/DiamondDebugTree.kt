package dev.anmatolay.template.compose.core.logging

import timber.log.Timber.DebugTree

// Append "<>" to debug logging for easier log filtering
class DiamondDebugTree : DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, "<> $message", t)
    }
}
