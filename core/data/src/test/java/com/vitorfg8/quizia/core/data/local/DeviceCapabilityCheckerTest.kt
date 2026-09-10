package com.vitorfg8.quizia.core.data.local

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DeviceCapabilityCheckerTest {

    private val mockPackageManager = mockk<PackageManager>()
    private val mockContext = mockk<Context> {
        every { packageManager } returns mockPackageManager
    }

    @Test
    fun `isGeminiNanoSupported returns false when the platform predates AICore`() {
        val actual = isGeminiNanoSupported(mockContext, sdkInt = Build.VERSION_CODES.TIRAMISU)
        assertFalse(actual)
    }

    @Test
    fun `isGeminiNanoSupported returns true when AICore is installed`() {
        every { mockPackageManager.getPackageInfo(AICORE_PACKAGE, 0) } returns PackageInfo()
        val actual = isGeminiNanoSupported(mockContext, sdkInt = SUPPORTED_SDK)
        assertTrue(actual)
    }

    @Test
    fun `isGeminiNanoSupported returns false when AICore is missing`() {
        every {
            mockPackageManager.getPackageInfo(AICORE_PACKAGE, 0)
        } throws PackageManager.NameNotFoundException()
        val actual = isGeminiNanoSupported(mockContext, sdkInt = SUPPORTED_SDK)
        assertFalse(actual)
    }

    private companion object {
        const val AICORE_PACKAGE = "com.google.android.aicore"
        const val SUPPORTED_SDK = Build.VERSION_CODES.UPSIDE_DOWN_CAKE
    }
}
