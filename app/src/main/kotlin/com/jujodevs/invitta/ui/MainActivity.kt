package com.jujodevs.invitta.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.jujodevs.invitta.core.designsystem.theme.InvittaTheme
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.authservice.api.AuthService
import com.jujodevs.invitta.library.googleauth.api.GoogleAuth
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private val googleAuth: GoogleAuth by inject()
    private val authService: AuthService by inject()
    private val googleClick: () -> Unit = {
        lifecycleScope.launch {
            val result =
                when (val resultGoogleAuth = googleAuth.login()) {
                    is Result.Error -> resultGoogleAuth.error.javaClass.simpleName
                    is Result.Success -> {
                        when (
                            val authResult =
                                authService.loginAndLinkWithGoogle(resultGoogleAuth.data)
                        ) {
                            is Result.Error -> authResult.error.javaClass.simpleName
                            is Result.Success -> authResult.data
                        }
                    }
                }
            Toast.makeText(
                this@MainActivity,
                result,
                Toast.LENGTH_SHORT,
            )
                .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.initialize()

        splashScreen.setKeepOnScreenCondition {
            !viewModel.state.isInitialized
        }

        lifecycleScope.launch {
            authService.loginAnonymously()
        }

        setContent {
            InvittaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TemporalScreen(
                        googleClick = googleClick,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun TemporalScreen(
    googleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        Button(googleClick) {
            Text(
                text = "INVITTA",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    InvittaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            TemporalScreen(
                googleClick = {},
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
