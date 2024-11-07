package dev.anmatolay.template.compose.core.analytic.impl

import android.os.Bundle
import dev.anmatolay.template.compose.core.analytic.AnalyticsWrapper

class FakeAnalyticsImpl() : AnalyticsWrapper {

    override fun setUserId(userId: String?) {
        // Do nothing
    }

    override fun setUserProperty(name: String, value: String) {
        // Do nothing
    }


    override fun logEven(name: String, bundle: Bundle) {
        // Do nothing
    }

}
