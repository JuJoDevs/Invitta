package com.jujodevs.invitta.feature.home.impl.presentation

import com.jujodevs.invitta.core.domain.model.event.Event

internal data class HomeState(
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
)
