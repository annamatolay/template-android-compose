package dev.anmatolay.template.compose.domain.usecase

import dev.anmatolay.template.compose.BaseTest
import dev.anmatolay.template.compose.core.util.Constants.DEFAULT_USER_ID
import dev.anmatolay.template.compose.core.util.MockUserPropertyImpl.TEST_VALUE_INT
import dev.anmatolay.template.compose.core.util.MockUserPropertyImpl.TEST_VALUE_STRING
import dev.anmatolay.template.compose.core.util.UserProperty.Companion.KEY_ANDROID_VERSION
import dev.anmatolay.template.compose.core.util.UserProperty.Companion.KEY_API_LEVEL
import dev.anmatolay.template.compose.core.util.UserProperty.Companion.KEY_APP_VERSION
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get

class MonitoringUseCaseTest : BaseTest() {

    private lateinit var useCase: MonitoringUseCase

    @Before
    override fun setUp() {
        super.setUp()
        useCase = MonitoringUseCase(get(), get(), get())
    }

    @Test
    fun `When setUserProperties Then set correct user properties`() {
        // When
        useCase.setUserProperties()

        // Then
        verify { fakeAnalytics.setUserProperty(KEY_APP_VERSION, TEST_VALUE_STRING) }
        verify { fakeAnalytics.setUserProperty(KEY_ANDROID_VERSION, TEST_VALUE_STRING) }
        verify { fakeAnalytics.setUserProperty(KEY_API_LEVEL, TEST_VALUE_INT.toString()) }
    }

    @Test
    fun `When setUpAnalyticsAndLogging Then set correct user ID`() {
        // When
        useCase.setUpAnalyticsAndLogging(DEFAULT_USER_ID)

        // Then
        verify { fakeAnalytics.setUserId(DEFAULT_USER_ID) }
    }
}
