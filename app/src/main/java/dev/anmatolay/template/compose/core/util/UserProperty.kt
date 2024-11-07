package dev.anmatolay.template.compose.core.util

import android.os.Build
import dev.anmatolay.template.compose.BuildConfig
import org.koin.core.annotation.Single

interface UserProperty {
    val version: String
    val osVersion: String
    val sdkVersion: Int

    companion object {
        const val KEY_APP_VERSION = "version_name"
        const val KEY_ANDROID_VERSION = "android_version"
        const val KEY_API_LEVEL = "api_level"
    }
}

@Single
data class UserPropertyImpl(
    override val version: String = BuildConfig.VERSION_NAME,
    override val osVersion: String = Build.VERSION.RELEASE,
    override val sdkVersion: Int = Build.VERSION.SDK_INT,
) : UserProperty

// android.os.Build not available during unit tests
data object MockUserPropertyImpl : UserProperty {
    const val TEST_VALUE_STRING = "mock"
    const val TEST_VALUE_INT = 1

    override val version = TEST_VALUE_STRING
    override val osVersion = TEST_VALUE_STRING
    override val sdkVersion = TEST_VALUE_INT
}
