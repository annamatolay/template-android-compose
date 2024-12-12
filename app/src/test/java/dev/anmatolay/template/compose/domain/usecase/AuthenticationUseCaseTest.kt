package dev.anmatolay.template.compose.domain.usecase

import dev.anmatolay.template.compose.BaseTest
import dev.anmatolay.template.compose.core.authentication.AuthenticatorWrapper
import dev.anmatolay.template.compose.core.util.Constants.DEFAULT_USER_ID
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verifyAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get
import kotlin.reflect.jvm.internal.impl.renderer.DescriptorRenderer.ValueParametersHandler.DEFAULT
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthenticationUseCaseTest : BaseTest() {
    private val userIdUseCase = mockk<UserIdUseCase>()
    private val authenticatorWrapper = mockk<AuthenticatorWrapper>()
    private val monitoringUseCase = mockk<MonitoringUseCase>()

    private lateinit var useCase: AuthenticationUseCase

    @Before
    override fun setUp() {
        super.setUp()
        useCase = AuthenticationUseCase(userIdUseCase, authenticatorWrapper, monitoringUseCase)
    }

    @Test
    fun `Given user with default id When userProvider changed Then cacheUserId and setUpAnalyticsAndLogging`() = runBlocking {
        every { userIdUseCase.retrieve() } returns flow { emit(DEFAULT_USER_ID) }
        every { userIdUseCase.cache("ID_UNKNOWN") } returns flow { emit(Unit) }
        every { authenticatorWrapper.signInAnonymously() } returns flow { emit(Result.success(DEFAULT_USER_ID)) }
        justRun { monitoringUseCase.setUpAnalyticsAndLogging(DEFAULT_USER_ID) }
        fakeAuthenticator.userId = DEFAULT_USER_ID
        fakeAuthenticator.isSuccessful = true

        // Then
        val result = useCase.signIn().first()

        // When
        verifyAll {
            userIdUseCase.cache(DEFAULT_USER_ID)
            monitoringUseCase.setUpAnalyticsAndLogging(DEFAULT_USER_ID)
        }

        assertTrue(result.isSuccess)
    }
}
