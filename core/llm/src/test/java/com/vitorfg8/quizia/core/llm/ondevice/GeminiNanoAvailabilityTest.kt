package com.vitorfg8.quizia.core.llm.ondevice

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GeminiNanoAvailabilityTest {

    private val mockSession = mockk<OnDeviceModelSession>()
    private val availability = GeminiNanoAvailability(mockSession)

    @Test
    fun `a supported device reports the model as available`() = runTest {
        coEvery { mockSession.isSupported() } returns true
        assertTrue(availability.isAvailable())
    }

    @Test
    fun `an unsupported device reports the model as unavailable`() = runTest {
        coEvery { mockSession.isSupported() } returns false
        assertFalse(availability.isAvailable())
    }

    @Test
    fun `a runtime failure reports the model as unavailable`() = runTest {
        coEvery { mockSession.isSupported() } throws IllegalStateException("aicore missing")
        assertFalse(availability.isAvailable())
    }
}
