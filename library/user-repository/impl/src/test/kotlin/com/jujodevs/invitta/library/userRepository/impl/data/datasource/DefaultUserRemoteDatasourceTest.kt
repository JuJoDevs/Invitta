package com.jujodevs.invitta.library.userRepository.impl.data.datasource

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.testing.verifyOnce
import com.jujodevs.invitta.library.remotedatabase.api.RemoteUserDatabase
import com.jujodevs.invitta.library.userRepository.impl.common.DEFAULT_USER_ID
import com.jujodevs.invitta.library.userRepository.impl.common.defaultUser
import com.jujodevs.invitta.library.userRepository.impl.common.defaultUserResponse
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultUserRemoteDatasourceTest {
    private lateinit var datasource: DefaultUserRemoteDatasource
    private val remoteUserDatabase: RemoteUserDatabase = mockk()

    @BeforeEach
    fun setup() {
        datasource = DefaultUserRemoteDatasource(remoteUserDatabase)
    }

    @Test
    fun `GIVEN RemoteUserDatabase returns success WHEN getUser THEN returns mapped success`() =
        runTest {
            val uid = DEFAULT_USER_ID
            val userResponse = defaultUserResponse
            val expectedUser = defaultUser
            every { remoteUserDatabase.getUser(uid) } returns flowOf(Result.Success(userResponse))

            val result = datasource.getUser(uid).first()

            result shouldBeEqualTo Result.Success(expectedUser)
            verifyOnce { remoteUserDatabase.getUser(uid) }
        }

    @Test
    fun `GIVEN RemoteUserDatabase returns error WHEN getUser THEN returns error`() =
        runTest {
            val uid = DEFAULT_USER_ID
            val error = DataError.RemoteDatabase.NO_INTERNET
            every { remoteUserDatabase.getUser(uid) } returns flowOf(Result.Error(error))

            val result = datasource.getUser(uid).first()

            result shouldBeEqualTo Result.Error(error)
            verifyOnce { remoteUserDatabase.getUser(uid) }
        }
}
