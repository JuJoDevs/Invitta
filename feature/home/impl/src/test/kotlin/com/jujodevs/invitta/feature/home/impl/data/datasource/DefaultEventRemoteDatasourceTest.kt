package com.jujodevs.invitta.feature.home.impl.data.datasource

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.testing.verifyOnce
import com.jujodevs.invitta.feature.home.impl.common.DEFAULT_USER_ID
import com.jujodevs.invitta.feature.home.impl.common.defaultAuthorizedEventMembers
import com.jujodevs.invitta.feature.home.impl.common.defaultEvent
import com.jujodevs.invitta.feature.home.impl.common.defaultEventResponse
import com.jujodevs.invitta.library.remotedatabase.api.RemoteEventDatabase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class DefaultEventRemoteDatasourceTest {
    private lateinit var datasource: DefaultEventRemoteDatasource
    private val remoteEventDatabase: RemoteEventDatabase = mockk()

    private val uid = DEFAULT_USER_ID
    private val authorizedMemberIds = defaultAuthorizedEventMembers.values.toList()

    @BeforeEach
    fun setup() {
        datasource = DefaultEventRemoteDatasource()
    }

    @Disabled("Temporarily disabled")
    @Test
    fun `GIVEN valid data WHEN getEvents THEN returns mapped events`() =
        runTest {
            val eventResponse = defaultEventResponse
            val expectedEvent = defaultEvent
            every { remoteEventDatabase.getEvents(uid, authorizedMemberIds) } returns
                flowOf(Result.Success(listOf(eventResponse)))

            val result = datasource.getEvents(uid, authorizedMemberIds).first()

            result shouldBeEqualTo Result.Success(listOf(expectedEvent))
            verifyOnce { remoteEventDatabase.getEvents(uid, authorizedMemberIds) }
        }

    @Disabled("Temporarily disabled")
    @Test
    fun `GIVEN error from remote database WHEN getEvents THEN returns failure`() =
        runTest {
            val expectedError = DataError.RemoteDatabase.NO_INTERNET
            every { remoteEventDatabase.getEvents(uid, authorizedMemberIds) } returns
                flowOf(Result.Error(expectedError))

            val result = datasource.getEvents(uid, authorizedMemberIds).first()

            result shouldBeEqualTo Result.Error(expectedError)
            verifyOnce { remoteEventDatabase.getEvents(uid, authorizedMemberIds) }
        }
}
