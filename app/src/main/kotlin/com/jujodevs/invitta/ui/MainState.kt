package com.jujodevs.invitta.ui

import com.jujodevs.invitta.core.domain.Error

internal data class MainState(
    val isLogged: Boolean = false,
    val error: Error? = null,
)
