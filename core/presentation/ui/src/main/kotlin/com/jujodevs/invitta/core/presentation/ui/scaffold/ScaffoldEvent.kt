package com.jujodevs.invitta.core.presentation.ui.scaffold

sealed interface ScaffoldEvent {
    data class UpdateScaffoldState(
        val scaffoldState: ScaffoldState,
    ) : ScaffoldEvent
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val onAction: () -> Unit = {},
    ) : ScaffoldEvent
}
