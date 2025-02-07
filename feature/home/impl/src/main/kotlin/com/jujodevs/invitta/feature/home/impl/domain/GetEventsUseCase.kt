package com.jujodevs.invitta.feature.home.impl.domain

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.domain.map
import com.jujodevs.invitta.core.domain.model.event.Event
import com.jujodevs.invitta.library.userRepository.api.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class GetEventsUseCase(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Result<List<Event>, DataError>> {
        return userRepository.getUser().flatMapConcat { userResult ->
            when (userResult) {
                is Result.Error -> flowOf<Result<List<Event>, DataError>>(userResult)
                is Result.Success -> {
                    val user = userResult.data
                    eventRepository.getEvents(
                        user.id,
                        user.authorizedEventMembers.values.toList(),
                    ).map { eventResult ->
                        eventResult.map { events ->
                            events.map { event ->
                                event.copy(userIsOrganizer = event.organizerId == user.id)
                            }
                        }
                    }
                }
            }
        }
    }
}
