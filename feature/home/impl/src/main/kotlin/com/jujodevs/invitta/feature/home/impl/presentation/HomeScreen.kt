package com.jujodevs.invitta.feature.home.impl.presentation

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jujodevs.invitta.core.presentation.stringresources.R
import com.jujodevs.invitta.core.presentation.ui.UiText
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeRoot(
    scaffoldViewModel: ScaffoldViewModel =
        koinViewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity),
) {
    scaffoldViewModel.onEvent(
        ScaffoldEvent.UpdateScaffoldState(
            ScaffoldState(
                topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            scaffoldViewModel.onEvent(
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

    InternalHomeScreen()
}

@Composable
internal fun InternalHomeScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        Text(
            text = "INVITTA",
        )
    }
}
