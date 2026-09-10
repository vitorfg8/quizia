package com.vitorfg8.quizia.core.data.repository

import com.vitorfg8.quizia.core.data.local.ApiKeyStore
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.repository.ApiKeyRepository

class ApiKeyRepositoryImpl(
    private val apiKeyStore: ApiKeyStore,
) : ApiKeyRepository {

    override fun getApiKey(provider: LlmProviderType): String? =
        apiKeyStore.getKey(provider)

    override fun saveApiKey(provider: LlmProviderType, key: String) {
        apiKeyStore.saveKey(provider, key)
    }

    override fun clearApiKey(provider: LlmProviderType) {
        apiKeyStore.clearKey(provider)
    }
}
