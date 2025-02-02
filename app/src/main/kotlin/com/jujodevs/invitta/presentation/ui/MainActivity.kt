package com.jujodevs.invitta.presentation.ui

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
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.jujodevs.invitta.core.designsystem.theme.InvittaTheme
import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.presentation.ui.ObserveAsEffects
import com.jujodevs.invitta.core.presentation.ui.asUiText
import com.jujodevs.invitta.core.presentation.ui.scaffold.ScaffoldEffect
import com.jujodevs.invitta.core.presentation.ui.scaffold.ScaffoldEvent
import com.jujodevs.invitta.core.presentation.ui.scaffold.ScaffoldViewModel
import com.jujodevs.invitta.presentation.navigation.Navigation
import com.jujodevs.invitta.presentation.navigation.Screens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()
    private val screens by inject<Screens>()

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
                val scaffoldViewModel = koinViewModel<ScaffoldViewModel>(viewModelStoreOwner = this)
                val snackbarHostState = remember { SnackbarHostState() }

                ObserveMainViewModelEffects(onEvent = scaffoldViewModel::onEvent)
                ObserveScaffoldViewModelEffects(
                    snackbarHostState = snackbarHostState,
                    effectFlow = scaffoldViewModel.effect,
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = scaffoldViewModel.state.topBar,
                    floatingActionButton = scaffoldViewModel.state.floatingActionButton,
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                ) { innerPadding ->
                    if (viewModel.state.isLogged) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                        ) {
                            Navigation(
                                navController = rememberNavController(),
                                screens = screens,
                            )
                        }
                    } else {
                        if (viewModel.state.error == null) {
                            viewModel.initialize()
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ObserveMainViewModelEffects(onEvent: (scaffoldEvent: ScaffoldEvent) -> Unit) {
        ObserveAsEffects(flow = viewModel.effect) { effect ->
            when (effect) {
                is MainEffect.ShowError ->
                    onEvent(
                        ScaffoldEvent.ShowSnackbar(
                            message = effect.error.asUiText(),
                            onAction =
                                if (effect.error is LoginError) {
                                    { finish() }
                                } else {
                                    { }
                                },
                        ),
                    )
            }
        }
    }

    @Composable
    private fun ObserveScaffoldViewModelEffects(
        snackbarHostState: SnackbarHostState,
        effectFlow: Flow<ScaffoldEffect>,
    ) {
        val scope = rememberCoroutineScope()
        ObserveAsEffects(flow = effectFlow) { effect ->
            when (effect) {
                is ScaffoldEffect.ShowSnackbar -> {
                    scope.launch {
                        val result =
                            snackbarHostState.showSnackbar(
                                message = effect.message.asString(this@MainActivity),
                                actionLabel = effect.actionLabel?.asString(this@MainActivity),
                            )
                        when (result) {
                            SnackbarResult.ActionPerformed -> effect.onAction()
                            SnackbarResult.Dismissed -> Unit
                        }
                    }
                }
            }
        }
    }
}
