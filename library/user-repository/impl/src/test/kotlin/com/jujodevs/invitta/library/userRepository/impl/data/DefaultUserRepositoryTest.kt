package com.jujodevs.invitta.library.userRepository.impl.data

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.testing.verifyOnce
import com.jujodevs.invitta.library.userRepository.impl.common.DEFAULT_USER_ID
import com.jujodevs.invitta.library.userRepository.impl.common.defaultUser
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultUserRepositoryTest {
    private lateinit var repository: DefaultUserRepository
    private val userAuthDatasource: UserAuthDatasource = mockk()
    private val userRemoteDatasource: UserRemoteDatasource = mockk()

    @BeforeEach
    fun setup() {
        repository = DefaultUserRepository(userAuthDatasource, userRemoteDatasource)
    }

    @Test
    fun `GIVEN valid UID WHEN getUser THEN returns user`() =
        runTest {
            val expectedUser = defaultUser
            every { userAuthDatasource.getUid() } returns DEFAULT_USER_ID
            every { userRemoteDatasource.getUser(DEFAULT_USER_ID) } returns flowOf(Result.Success(expectedUser))

            val result = repository.getUser().first()

            result shouldBeEqualTo Result.Success(expectedUser)
            verifyOnce { userAuthDatasource.getUid() }
            verifyOnce { userRemoteDatasource.getUser(DEFAULT_USER_ID) }
        }

    @Test
    fun `GIVEN userRemoteDatasource returns error WHEN getUser THEN returns error`() =
        runTest {
            val expectedError = DataError.RemoteDatabase.NO_INTERNET
            every { userAuthDatasource.getUid() } returns DEFAULT_USER_ID
            every { userRemoteDatasource.getUser(DEFAULT_USER_ID) } returns flowOf(Result.Error(expectedError))

            val result = repository.getUser().first()

            result shouldBeEqualTo Result.Error(expectedError)
            verifyOnce { userAuthDatasource.getUid() }
            verifyOnce { userRemoteDatasource.getUser(DEFAULT_USER_ID) }
        }
}
