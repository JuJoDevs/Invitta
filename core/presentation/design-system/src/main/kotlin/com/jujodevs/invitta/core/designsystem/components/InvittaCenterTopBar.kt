package com.jujodevs.invitta.core.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.jujodevs.invitta.core.designsystem.theme.Dimens
import com.jujodevs.invitta.core.designsystem.theme.InvittaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvittaCenterTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    val density = LocalDensity.current
    var navIconWidth by remember { mutableStateOf(0.dp) }
    var actionsWidth by remember { mutableStateOf(0.dp) }
    val maxWidth by remember {
        derivedStateOf { maxOf(navIconWidth, actionsWidth, Dimens.mediumLarge) }
    }
    TopAppBar(
        navigationIcon = {
            Box(
                modifier =
                    Modifier
                        .sizeIn(minWidth = maxWidth)
                        .onGloballyPositioned { layoutCoordinates ->
                            navIconWidth =
                                with(density) {
                                    layoutCoordinates.size.width.toDp()
                                }
                        },
            ) {
                navigationIcon()
            }
        },
        title = {
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                text = title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        actions = {
            Box(
                modifier =
                    Modifier
                        .sizeIn(minWidth = maxWidth)
                        .onGloballyPositioned { layoutCoordinates ->
                            actionsWidth =
                                with(density) {
                                    layoutCoordinates.size.width.toDp()
                                }
                        },
            ) {
                Row { actions() }
            }
        },
        modifier = modifier,
    )
}

@PreviewLightDark
@Preview
@Composable
private fun InvitttaCenterTopBarPreview() {
    InvittaTheme {
        Scaffold(
            topBar = {
                InvittaCenterTopBar(
                    title = "INVITTA",
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    },
                )
            },
        ) {
            Box(Modifier.fillMaxSize().padding(it))
        }
    }
}
