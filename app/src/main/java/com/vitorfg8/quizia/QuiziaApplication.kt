package com.vitorfg8.quizia

import android.app.Application
import com.vitorfg8.quizia.core.data.di.dataModule
import com.vitorfg8.quizia.di.appModule
import com.vitorfg8.quizia.feature.home.di.homeModule
import com.vitorfg8.quizia.feature.llmsetup.di.llmSetupModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class QuiziaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@QuiziaApplication)
            modules(
                appModule,
                dataModule,
                llmSetupModule,
                homeModule,
            )
        }
    }
}
