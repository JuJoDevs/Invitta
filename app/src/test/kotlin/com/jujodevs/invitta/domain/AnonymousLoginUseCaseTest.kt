package com.jujodevs.invitta.domain

import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.testing.coVerifyOnce
import com.jujodevs.invitta.library.authservice.api.AuthService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class AnonymousLoginUseCaseTest {
    private val authService: AuthService = mockk()
    private val useCase = AnonymousLoginUseCase(authService)

    @Test
    fun `GIVEN user is not logged in WHEN invoke is called THEN it attempts anonymous login and returns success`() =
        runTest {
            val uid = "uid"
            coEvery { authService.isUserLogged() } returns false
            coEvery { authService.loginAnonymously() } returns Result.Success(uid)

            val result = useCase()

            coVerifyOnce { authService.isUserLogged() }
            coVerifyOnce { authService.loginAnonymously() }
            result shouldBeEqualTo Result.Success(Unit)
            confirmVerified(authService)
        }

    @Test
    fun `GIVEN user is already logged in WHEN invoke is called THEN it returns success without attempting login`() =
        runTest {
            coEvery { authService.isUserLogged() } returns true

            val result = useCase()

            coVerifyOnce { authService.isUserLogged() }
            coVerify(exactly = 0) { authService.loginAnonymously() }
            result shouldBeEqualTo Result.Success(Unit)
            confirmVerified(authService)
        }

    @Test
    fun `GIVEN anonymous login fails WHEN invoke is called THEN it returns an error`() =
        runTest {
            val loginError = LoginError.AuthService.ANONYMOUS_LOGIN_FAILED
            coEvery { authService.isUserLogged() } returns false
            coEvery { authService.loginAnonymously() } returns Result.Error(loginError)

            val result = useCase()

            coVerifyOnce { authService.isUserLogged() }
            coVerifyOnce { authService.loginAnonymously() }
            result shouldBeInstanceOf Result.Error::class
            (result as Result.Error).error shouldBeEqualTo loginError
            confirmVerified(authService)
        }
}
