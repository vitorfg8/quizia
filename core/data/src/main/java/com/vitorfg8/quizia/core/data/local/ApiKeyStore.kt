package com.vitorfg8.quizia.core.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.vitorfg8.quizia.core.domain.model.LlmProviderType

private const val PREFS_FILE_NAME = "quizia_api_keys"

class ApiKeyStore(context: Context) {

    private val masterKey = MasterKey.Builder(context.applicationContext)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context.applicationContext,
        PREFS_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    fun getKey(provider: LlmProviderType): String? =
        sharedPreferences.getString(provider.name, null)

    fun saveKey(provider: LlmProviderType, key: String) {
        sharedPreferences.edit().putString(provider.name, key).apply()
    }

    fun clearKey(provider: LlmProviderType) {
        sharedPreferences.edit().remove(provider.name).apply()
    }
}
