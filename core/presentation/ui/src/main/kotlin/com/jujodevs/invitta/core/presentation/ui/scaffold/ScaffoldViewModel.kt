package com.jujodevs.invitta.core.presentation.ui.scaffold

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ScaffoldViewModel : ViewModel() {
    var state by mutableStateOf(ScaffoldState())
        private set

    private val _effect = Channel<ScaffoldEffect>()
    val effect = _effect.receiveAsFlow()

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
            _effect.send(ScaffoldEffect.ShowSnackbar(message, actionLabel, onAction))
        }
    }
}
