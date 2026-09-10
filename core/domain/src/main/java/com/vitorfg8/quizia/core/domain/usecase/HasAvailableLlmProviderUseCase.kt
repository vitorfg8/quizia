package com.vitorfg8.quizia.core.domain.usecase

import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.repository.ApiKeyRepository
import com.vitorfg8.quizia.core.domain.repository.OnDeviceModelRepository

/** True when Gemini Nano can run or any cloud provider has a stored API key. */
class HasAvailableLlmProviderUseCase(
    private val apiKeyRepository: ApiKeyRepository,
    private val onDeviceModelRepository: OnDeviceModelRepository,
) {
    suspend operator fun invoke(): Boolean {
        if (onDeviceModelRepository.isAvailable()) return true
        return CLOUD_PROVIDERS.any { provider ->
            !apiKeyRepository.getApiKey(provider).isNullOrBlank()
        }
    }

    private companion object {
        val CLOUD_PROVIDERS: List<LlmProviderType> =
            LlmProviderType.entries.filter { type -> type != LlmProviderType.GEMINI_NANO }
    }
}
