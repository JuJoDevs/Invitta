package com.jujodevs.invitta.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jujodevs.invitta.core.designsystem.theme.InvittaTheme
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.presentation.ui.ObserveAsEffects
import com.jujodevs.invitta.core.presentation.ui.asUiText
import com.jujodevs.invitta.core.stringresources.R
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.initialize()

        splashScreen.setKeepOnScreenCondition {
            !(viewModel.state.isLogged || viewModel.state.error != null)
        }

        setContent {
            InvittaTheme {
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                ObserveAsEffects(flow = viewModel.effect) { effect ->
                    when (effect) {
                        is MainEffect.ShowError ->
                            scope.launch {
                                showErrorInSnackbar(
                                    snackbarHostState,
                                    effect,
                                )
                            }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                ) { innerPadding ->
                    TemporalScreen(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }

    private suspend fun showErrorInSnackbar(
        snackbarHostState: SnackbarHostState,
        effect: MainEffect.ShowError,
    ) {
        snackbarHostState.showSnackbar(
            message =
                when (val error = effect.error) {
                    is LoginError ->
                        error.asUiText()
                            .asString(this)

                    is DataError ->
                        error.asUiText()
                            .asString(this)

                    else -> getString(R.string.unknown_error)
                },
            actionLabel = getString(R.string.ok),
        )
    }
}

@Composable
fun TemporalScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        Text(
            text = "INVITTA",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    InvittaTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            TemporalScreen(
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
