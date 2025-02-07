package com.jujodevs.invitta.feature.home.impl.domain

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.testing.verifyNever
import com.jujodevs.invitta.core.testing.verifyOnce
import com.jujodevs.invitta.feature.home.impl.common.DEFAULT_USER_ID
import com.jujodevs.invitta.feature.home.impl.common.defaultAuthorizedEventMembers
import com.jujodevs.invitta.feature.home.impl.common.defaultEvent
import com.jujodevs.invitta.feature.home.impl.common.defaultUser
import com.jujodevs.invitta.library.userRepository.api.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetEventsUseCaseTest {
    private lateinit var useCase: GetEventsUseCase
    private val userRepository: UserRepository = mockk()
    private val eventRepository: EventRepository = mockk()

    private val uid = DEFAULT_USER_ID
    private val authorizedMemberIds = defaultAuthorizedEventMembers.values.toList()

    @BeforeEach
    fun setup() {
        useCase = GetEventsUseCase(userRepository, eventRepository)
    }

    @Test
    fun `GIVEN user data WHEN invoke THEN returns events with organizer flag`() =
        runTest {
            val user = defaultUser
            val event = defaultEvent.copy(organizerId = user.id)
            val events = listOf(event)
            val expectedEvents = listOf(event.copy(userIsOrganizer = true))

            every { userRepository.getUser() } returns flowOf(Result.Success(user))
            every { eventRepository.getEvents(uid, authorizedMemberIds) } returns
                flowOf(
                    Result.Success(events),
                )

            val result = useCase().first()

            result shouldBeEqualTo Result.Success(expectedEvents)
            verifyOnce { userRepository.getUser() }
            verifyOnce { eventRepository.getEvents(uid, authorizedMemberIds) }
        }

    @Test
    fun `GIVEN user retrieval fails WHEN invoke THEN returns error`() =
        runTest {
            val error = DataError.RemoteDatabase.NO_INTERNET
            every { userRepository.getUser() } returns flowOf(Result.Error(error))

            val result = useCase().first()

            result shouldBeEqualTo Result.Error(error)
            verifyOnce { userRepository.getUser() }
            verifyNever { eventRepository.getEvents(any(), any()) }
        }

    @Test
    fun `GIVEN event retrieval fails WHEN invoke THEN returns error`() =
        runTest {
            val error = DataError.RemoteDatabase.NO_INTERNET
            every { userRepository.getUser() } returns flowOf(Result.Success(defaultUser))
            every { eventRepository.getEvents(uid, authorizedMemberIds) } returns
                flowOf(
                    Result.Error(error),
                )

            val result = useCase().first()

            result shouldBeEqualTo Result.Error(error)
            verifyOnce { userRepository.getUser() }
            verifyOnce { eventRepository.getEvents(uid, authorizedMemberIds) }
        }
}
