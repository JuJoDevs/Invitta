package com.jujodevs.invitta.core.presentation.ui.scaffold

import com.jujodevs.invitta.core.presentation.stringresources.R
import com.jujodevs.invitta.core.presentation.ui.UiText

sealed interface ScaffoldEvent {
    data class UpdateScaffoldState(
        val scaffoldState: ScaffoldState,
    ) : ScaffoldEvent
    data class ShowSnackbar(
        val message: UiText,
        val actionLabel: UiText? = UiText.StringResource(R.string.ok),
        val onAction: () -> Unit = {},
    ) : ScaffoldEvent
}
