package com.jujodevs.invitta.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jujodevs.invitta.core.designsystem.theme.InvittaTheme
import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.presentation.stringresources.R
import com.jujodevs.invitta.core.presentation.ui.ObserveAsEffects
import com.jujodevs.invitta.core.presentation.ui.getErrorMessage
import com.jujodevs.invitta.core.presentation.ui.scaffold.ScaffoldEvent
import com.jujodevs.invitta.core.presentation.ui.scaffold.ScaffoldState
import com.jujodevs.invitta.core.presentation.ui.scaffold.ScaffoldViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()
    private val scaffoldViewModel by viewModel<ScaffoldViewModel>()

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
                ObserveAsEffects(flow = viewModel.effect) { effect ->
                    when (effect) {
                        is MainEffect.ShowError ->
                            scaffoldViewModel.onEvent(
                                ScaffoldEvent.ShowSnackbar(
                                    getErrorMessage(effect.error),
                                    getString(R.string.ok),
                                    if (effect.error is LoginError) {
                                        { finish() }
                                    } else {
                                        { }
                                    },
                                ),
                            )
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = scaffoldViewModel.state.topBar,
                    floatingActionButton = scaffoldViewModel.state.floatingActionButton,
                    snackbarHost = { SnackbarHost(scaffoldViewModel.snackbarHostState) },
                ) { innerPadding ->
                    if (viewModel.state.isLogged) {
                        TemporalScreen(
                            modifier = Modifier.padding(innerPadding),
                            scaffoldViewModel = scaffoldViewModel,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemporalScreen(
    scaffoldViewModel: ScaffoldViewModel,
    modifier: Modifier = Modifier,
) {
    scaffoldViewModel.onEvent(
        ScaffoldEvent.UpdateScaffoldState(
            ScaffoldState(
                topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        scaffoldViewModel.onEvent(ScaffoldEvent.ShowSnackbar("Test", null))
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                },
            ),
        ),
    )

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
                scaffoldViewModel = ScaffoldViewModel(SnackbarHostState()),
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
