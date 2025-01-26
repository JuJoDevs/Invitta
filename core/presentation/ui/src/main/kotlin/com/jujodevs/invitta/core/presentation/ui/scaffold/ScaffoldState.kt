package com.jujodevs.invitta.core.presentation.ui.scaffold

import androidx.compose.runtime.Composable

class ScaffoldState(
    var topBar: @Composable () -> Unit = {},
    var floatingActionButton: @Composable () -> Unit = {},
)
