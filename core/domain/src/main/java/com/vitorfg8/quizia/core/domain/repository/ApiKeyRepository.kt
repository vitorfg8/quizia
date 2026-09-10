package com.vitorfg8.quizia.core.domain.repository

import com.vitorfg8.quizia.core.domain.model.LlmProviderType

interface ApiKeyRepository {
    fun getApiKey(provider: LlmProviderType): String?
    fun saveApiKey(provider: LlmProviderType, key: String)
    fun clearApiKey(provider: LlmProviderType)
}
