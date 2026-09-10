package com.vitorfg8.quizia.feature.settings

private const val VISIBLE_SUFFIX_LENGTH = 4
private const val MASK_PREFIX = "••••••••"

/** Keeps only the last characters of a stored key so the UI never shows the full secret. */
internal fun maskApiKey(key: String?): String {
    if (key.isNullOrBlank()) return ""
    return MASK_PREFIX + key.takeLast(VISIBLE_SUFFIX_LENGTH)
}
