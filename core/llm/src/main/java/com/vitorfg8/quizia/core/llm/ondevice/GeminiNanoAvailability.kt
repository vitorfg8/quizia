package com.vitorfg8.quizia.core.llm.ondevice

import com.vitorfg8.quizia.core.domain.repository.OnDeviceModelRepository

/**
 * Answers whether Gemini Nano can be offered to the user. A runtime failure is treated as
 * "not available" so the setup screen simply hides the option instead of breaking.
 */
internal class GeminiNanoAvailability(
    private val session: OnDeviceModelSession,
) : OnDeviceModelRepository {

    override suspend fun isAvailable(): Boolean =
        runCatching { session.isSupported() }.getOrDefault(false)
}
