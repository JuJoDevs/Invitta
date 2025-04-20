package com.jujodevs.invitta.feature.home.impl.domain

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.domain.model.event.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

internal class GetEventsUseCase(
    private val eventRepository: EventRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Result<List<Event>, DataError>> {
        return eventRepository.getEvents("", emptyList())
    }
}
