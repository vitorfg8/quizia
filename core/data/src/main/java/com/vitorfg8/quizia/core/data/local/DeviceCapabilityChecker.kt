package com.vitorfg8.quizia.core.data.local

import android.content.Context
import android.os.Build

/** System package that hosts the on-device Gemini Nano runtime. */
private const val AICORE_PACKAGE = "com.google.android.aicore"

/** First platform version that ships the AICore runtime. */
private const val MIN_NANO_SDK = Build.VERSION_CODES.UPSIDE_DOWN_CAKE

/**
 * Checks whether the device supports on-device Gemini Nano inference.
 * Nano is served exclusively by the AICore system package, so its presence on a recent
 * enough platform is the support signal.
 */
internal fun isGeminiNanoSupported(
    context: Context,
    sdkInt: Int = Build.VERSION.SDK_INT,
): Boolean {
    if (sdkInt < MIN_NANO_SDK) return false
    @Suppress("DEPRECATION")
    return runCatching { context.packageManager.getPackageInfo(AICORE_PACKAGE, 0) }.isSuccess
}
