package com.vitorfg8.quizia.core.domain.repository

import com.vitorfg8.quizia.core.domain.model.AppSettings
import com.vitorfg8.quizia.core.domain.model.AppTheme
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun observeSettings(): Flow<AppSettings>
    suspend fun saveSelectedProvider(provider: LlmProviderType)
    suspend fun saveTheme(theme: AppTheme)
    suspend fun saveQuestionCount(count: Int)
    suspend fun setFirstRunComplete()
}
