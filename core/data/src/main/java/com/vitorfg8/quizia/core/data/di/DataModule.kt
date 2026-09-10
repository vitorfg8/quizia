package com.vitorfg8.quizia.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.vitorfg8.quizia.core.data.local.ApiKeyStore
import com.vitorfg8.quizia.core.data.repository.ApiKeyRepositoryImpl
import com.vitorfg8.quizia.core.data.repository.SettingsRepositoryImpl
import com.vitorfg8.quizia.core.domain.repository.ApiKeyRepository
import com.vitorfg8.quizia.core.domain.repository.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import androidx.datastore.preferences.core.PreferenceDataStoreFactory

val dataModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile("app_preferences") }
        )
    }
    single { ApiKeyStore(androidContext()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    single<ApiKeyRepository> { ApiKeyRepositoryImpl(get()) }
}
