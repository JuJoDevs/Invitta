package com.jujodevs.invitta.core.presentation.ui.scaffold

import com.jujodevs.invitta.core.presentation.ui.UiText

sealed interface ScaffoldEffect {
    data class ShowSnackbar(
        val message: UiText,
        val actionLabel: UiText?,
        val onAction: () -> Unit = {},
    ) : ScaffoldEffect
}
