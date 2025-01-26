package com.jujodevs.invitta.core.presentation.ui.scaffold

sealed interface ScaffoldEffect {
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String?,
        val onAction: () -> Unit = {},
    ) : ScaffoldEffect
}
