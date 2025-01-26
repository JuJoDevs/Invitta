package com.jujodevs.invitta.core.presentation.ui.scaffold

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ScaffoldViewModel(
    val snackbarHostState: SnackbarHostState,
) : ViewModel() {
    var state by mutableStateOf(ScaffoldState())
        private set

    fun onEvent(event: ScaffoldEvent) {
        when (event) {
            is ScaffoldEvent.UpdateScaffoldState ->
                updateScaffoldState(event.scaffoldState)

            is ScaffoldEvent.ShowSnackbar ->
                showSnackbar(event.message, event.actionLabel, event.onAction)
        }
    }

    private fun updateScaffoldState(state: ScaffoldState) {
        this.state = state
    }

    private fun showSnackbar(
        message: String,
        actionLabel: String?,
        onAction: () -> Unit = {},
    ) {
        viewModelScope.launch {
            val result =
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel,
                )
            when (result) {
                SnackbarResult.ActionPerformed -> onAction()
                SnackbarResult.Dismissed -> Unit
            }
        }
    }
}
