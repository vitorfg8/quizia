package com.vitorfg8.quizia.feature.settings

import org.junit.Assert.assertEquals
import org.junit.Test

class ApiKeyMaskTest {

    @Test
    fun `a stored key keeps only the last four characters visible`() {
        val actual = maskApiKey("sk-abcdefghijklmnop1234")
        assertEquals("••••••••1234", actual)
    }

    @Test
    fun `a short key is still prefixed with the mask`() {
        val actual = maskApiKey("ab")
        assertEquals("••••••••ab", actual)
    }

    @Test
    fun `a missing key produces an empty mask`() {
        val actual = maskApiKey(null)
        assertEquals("", actual)
    }

    @Test
    fun `a blank key produces an empty mask`() {
        val actual = maskApiKey("   ")
        assertEquals("", actual)
    }
}
