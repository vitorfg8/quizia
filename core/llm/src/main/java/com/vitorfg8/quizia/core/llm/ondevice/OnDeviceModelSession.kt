package com.vitorfg8.quizia.core.llm.ondevice

/**
 * Thin seam over the on-device model runtime.
 * Keeping the ML Kit types behind this interface lets the Nano provider be unit-tested on the
 * JVM, since the runtime itself only exists on a real device.
 */
internal interface OnDeviceModelSession {

    /** Whether this device can run the model at all, even if it still has to be downloaded. */
    suspend fun isSupported(): Boolean

    /** Makes the model usable, downloading it when necessary. Returns false when it cannot be. */
    suspend fun prepare(): Boolean

    suspend fun generate(prompt: String): String?
}
