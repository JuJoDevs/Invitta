package com.jujodevs.invitta.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    fun initialize() {
        viewModelScope.launch {
            delay(INITIALIZE_DELAY)
            state = state.copy(isInitialized = true)
        }
    }
}

private const val INITIALIZE_DELAY = 2000L
