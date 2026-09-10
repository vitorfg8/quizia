package com.vitorfg8.quizia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitorfg8.quizia.core.domain.model.AppTheme
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.nav.AppNavGraph
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = koinViewModel()
            val isFirstRun by viewModel.isFirstRun.collectAsStateWithLifecycle()
            val theme by viewModel.theme.collectAsStateWithLifecycle()

            QuiziaTheme(darkTheme = theme.isDarkTheme()) {
                if (isFirstRun != null) {
                    AppNavGraph(isFirstRun = isFirstRun!!)
                }
            }
        }
    }
}

@Composable
private fun AppTheme.isDarkTheme(): Boolean = when (this) {
    AppTheme.LIGHT -> false
    AppTheme.DARK -> true
    AppTheme.SYSTEM -> isSystemInDarkTheme()
}
