package dev.anmatolay.template.compose.data.source.local


import dev.anmatolay.template.compose.BaseTest
import dev.anmatolay.template.compose.core.util.Constants.DEFAULT_USER_ID
import dev.anmatolay.template.compose.core.util.Constants.KEY_USER_ID
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserIdDataSourceTest : BaseTest() {

    private val dataSource = UserIdDataSource(sharedPrefHandler)

    @Test
    fun `Given user not cached When getUserId called Then return with null`() = runBlocking {
        // Given
        every { sharedPrefHandler.getString(KEY_USER_ID) } returns null

        // When
        val result = dataSource.getUserId().first()

        // Then
        assertNull(result)
    }

    @Test
    fun `Given user cached When getUserId called Then return with ID`() = runBlocking {
        // Given
        every { sharedPrefHandler.getString(KEY_USER_ID) } returns DEFAULT_USER_ID

        // When
        val result = dataSource.getUserId().first()

        // Then
        assertEquals(DEFAULT_USER_ID, result)
    }
//
    @Test
    fun `Given string ID When putUserId called Then saved with shared pref`() = runBlocking {
        // Given
        justRun { sharedPrefHandler.putString(KEY_USER_ID, DEFAULT_USER_ID) }

        // When
        dataSource.putUserId(DEFAULT_USER_ID).first()

        // Then
        verify { sharedPrefHandler.putString(KEY_USER_ID, DEFAULT_USER_ID) }
    }
}
