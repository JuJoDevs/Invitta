package com.jujodevs.invitta.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.domain.Result.Error
import com.jujodevs.invitta.domain.AnonymousLoginUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

internal class MainViewModel(
    private val anonymousLoginUseCase: AnonymousLoginUseCase,
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    private val _effect: Channel<MainEffect> = Channel()
    val effect: Flow<MainEffect> = _effect.receiveAsFlow()

    fun initialize() {
        viewModelScope.launch {
            state =
                when (val result = anonymousLoginUseCase()) {
                    is Result.Success -> {
                        state.copy(isLogged = true)
                    }

                    is Error -> {
                        _effect.send(MainEffect.ShowError(result.error))
                        state.copy(error = result.error)
                    }
                }
        }
    }
}
