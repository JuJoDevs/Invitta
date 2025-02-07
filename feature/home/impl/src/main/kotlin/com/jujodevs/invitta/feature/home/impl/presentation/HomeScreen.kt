package com.jujodevs.invitta.feature.home.impl.presentation

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jujodevs.invitta.core.designsystem.components.InvittaCenterTopBar
import com.jujodevs.invitta.core.presentation.stringresources.R
import com.jujodevs.invitta.core.presentation.ui.LaunchOnStart
import com.jujodevs.invitta.core.presentation.ui.ObserveAsEffects
import com.jujodevs.invitta.core.presentation.ui.UiText
import com.jujodevs.invitta.core.presentation.ui.asUiText
import com.jujodevs.invitta.core.presentation.ui.scaffold.ScaffoldEvent
import com.jujodevs.invitta.core.presentation.ui.scaffold.ScaffoldState
import com.jujodevs.invitta.core.presentation.ui.scaffold.ScaffoldViewModel
import org.koin.androidx.compose.koinViewModel

class HomeScreen {
    @Composable
    fun Screen() {
        HomeRoot()
    }
}

@Composable
internal fun HomeRoot(
    homeViewModel: HomeViewModel = koinViewModel(),
    scaffoldViewModel: ScaffoldViewModel =
        koinViewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity),
) {
    setScaffold { scaffoldViewModel.onEvent(it) }

    LaunchOnStart {
        homeViewModel.initialize()
    }

    ObserveAsEffects(homeViewModel.effect) { effect ->
        when (effect) {
            is HomeEffect.ShowError ->
                scaffoldViewModel.onEvent(ScaffoldEvent.ShowSnackbar(effect.error.asUiText()))
        }
    }

    InternalHomeScreen(homeViewModel.state)
}

internal fun setScaffold(onEvent: (ScaffoldEvent) -> Unit) {
    onEvent(
        ScaffoldEvent.UpdateScaffoldState(
            ScaffoldState(
                topBar = {
                    InvittaCenterTopBar(title = stringResource(R.string.app_name))
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            onEvent(
                                ScaffoldEvent.ShowSnackbar(
                                    UiText.DynamicString("Test"),
                                    null,
                                ),
                            )
                        },
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add",
                        )
                    }
                },
            ),
        ),
    )
}

@Composable
internal fun InternalHomeScreen(state: HomeState) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        LazyColumn {
            items(
                items = state.events,
                key = { it.id },
            ) { event ->
                Text("User is organizer ${event.userIsOrganizer}")
            }
        }
    }
}
