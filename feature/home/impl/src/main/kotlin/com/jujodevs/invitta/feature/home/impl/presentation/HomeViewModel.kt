package com.jujodevs.invitta.feature.home.impl.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.feature.home.impl.domain.GetEventsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.plus

internal class HomeViewModel(
    private val getEventsUseCase: GetEventsUseCase,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    private val _effect: Channel<HomeEffect> = Channel()
    val effect: Flow<HomeEffect> = _effect.receiveAsFlow()

    fun initialize() {
        state = state.copy(isLoading = true)

        getEventsUseCase()
            .onEach { result ->
                when (result) {
                    is Result.Error -> {
                        state = state.copy(isLoading = false)
                        _effect.send(HomeEffect.ShowError(result.error))
                    }
                    is Result.Success -> {
                        state =
                            state.copy(
                                isLoading = false,
                                events = result.data,
                            )
                    }
                }
            }
            .launchIn(viewModelScope + dispatcher)
    }
}
