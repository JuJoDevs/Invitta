package com.jujodevs.invitta.feature.home.impl.data

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.testing.verifyOnce
import com.jujodevs.invitta.feature.home.impl.common.DEFAULT_USER_ID
import com.jujodevs.invitta.feature.home.impl.common.defaultAuthorizedEventMembers
import com.jujodevs.invitta.feature.home.impl.common.defaultEvent
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultEventRepositoryTest {
    private lateinit var repository: DefaultEventRepository
    private val remoteDatasource: EventRemoteDatasource = mockk()

    private val uid = DEFAULT_USER_ID
    private val authorizedMemberIds = defaultAuthorizedEventMembers.values.toList()

    @BeforeEach
    fun setup() {
        repository = DefaultEventRepository(remoteDatasource)
    }

    @Test
    fun `GIVEN valid data WHEN getEvents THEN returns mapped events`() =
        runTest {
            val expectedEvents = listOf(defaultEvent, defaultEvent.copy(id = "event_2"))
            every { remoteDatasource.getEvents(uid, authorizedMemberIds) } returns
                flowOf(
                    Result.Success(expectedEvents),
                )

            val result = repository.getEvents(uid, authorizedMemberIds).first()

            result shouldBeEqualTo Result.Success(expectedEvents)
            verifyOnce { remoteDatasource.getEvents(uid, authorizedMemberIds) }
        }

    @Test
    fun `GIVEN error from remote datasource WHEN getEvents THEN returns failure`() =
        runTest {
            val error = DataError.RemoteDatabase.NO_INTERNET
            every { remoteDatasource.getEvents(uid, authorizedMemberIds) } returns flowOf(Result.Error(error))

            val result = repository.getEvents(uid, authorizedMemberIds).first()

            result shouldBeEqualTo Result.Error(error)
            verifyOnce { remoteDatasource.getEvents(uid, authorizedMemberIds) }
        }
}
