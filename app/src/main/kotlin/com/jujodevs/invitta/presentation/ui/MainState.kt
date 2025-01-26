package com.jujodevs.invitta.presentation.ui

import com.jujodevs.invitta.core.domain.Error

internal data class MainState(
    val isLogged: Boolean = false,
    val error: Error? = null,
)
