package com.jujodevs.invitta.core.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.jujodevs.invitta.core.designsystem.theme.Dimens
import com.jujodevs.invitta.core.designsystem.theme.InvittaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvittaTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        navigationIcon = { navigationIcon() },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        actions = { actions() },
        modifier = modifier,
    )
}

@PreviewLightDark
@Preview
@Composable
private fun InvitttaTopBarPreview() {
    InvittaTheme {
        Scaffold(
            topBar = {
                InvittaTopBar(
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    title = "Event Detail",
                    actions = {
                        InvittaButton(label = "Edit", onClick = {})
                        InvittaButton(
                            label = "Delete",
                            onClick = {},
                            modifier = Modifier.padding(horizontal = Dimens.medium),
                        )
                    },
                )
            },
        ) {
            Box(Modifier.fillMaxSize().padding(it))
        }
    }
}
