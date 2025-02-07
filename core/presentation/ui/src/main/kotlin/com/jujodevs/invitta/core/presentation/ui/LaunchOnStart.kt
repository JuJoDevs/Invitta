package com.jujodevs.invitta.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

@Composable
fun LaunchOnStart(block: () -> Unit) {
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        block()
    }
}
