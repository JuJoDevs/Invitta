package com.jujodevs.invitta.library.authservice.impl

import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.testing.coVerifyOnce
import com.jujodevs.invitta.core.testing.rule.CoroutineTestRule
import com.jujodevs.invitta.library.logger.api.Logger
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.AuthConfig
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
class SupabaseAuthServiceTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var authService: SupabaseAuthService
    private val auth = mockk<Auth>()
    private val config = mockk<AuthConfig>()
    private val logger = mockk<Logger>()

    @Before
    fun setUp() {
        authService = SupabaseAuthService(auth, logger)
        every { logger.e(any(), any(), any()) } returns Unit
        every { auth.config } returns config
        every { config.defaultRedirectUrl } returns "http://localhost"
    }

    @Test
    fun `GIVEN anonymous login success WHEN loginAnonymously THEN return user UID`() =
        runTest {
            val user =
                mockk<UserInfo> {
                    every { id } returns "uid123"
                }
            coEvery { auth.signInAnonymously(any(), any()) } returns Unit
            every { auth.currentUserOrNull() } returns user

            val result = authService.loginAnonymously()

            result shouldBeEqualTo Result.Success("uid123")
        }

    @Test
    fun `GIVEN anonymous login returns null WHEN loginAnonymously THEN return ANONYMOUS_LOGIN_FAILED`() =
        runTest {
            coEvery { auth.signInAnonymously(any(), any()) } returns Unit
            every { auth.currentUserOrNull() } returns null

            val result = authService.loginAnonymously()

            result shouldBeEqualTo Result.Error(LoginError.AuthService.ANONYMOUS_LOGIN_FAILED)
        }

    @Test
    fun `GIVEN no email in token WHEN loginAndLinkWithGoogle THEN return EMAIL_CLAIM_NOT_FOUND`() =
        runTest {
            val token = "header.payload.signature" // payload sin email

            val result = authService.loginAndLinkWithGoogle(token)

            result shouldBeEqualTo Result.Error(LoginError.GoogleAuth.EMAIL_CLAIM_NOT_FOUND)
        }

    @Test
    fun `GIVEN valid email WHEN loginAndLinkWithGoogle THEN call updateUser and return UID`() =
        runTest {
            val email = "email@test.com"
            val token = buildIdToken(email)
            val user = mockk<UserInfo> { every { id } returns "linked_uid" }

            coEvery { auth.updateUser(any(), any(), any()) } returns user
            coEvery {
                auth.signInWith(any<Google>(), any(), any())
            } returns Unit
            every { auth.currentUserOrNull()?.id } returns "linked_uid"

            val result = authService.loginAndLinkWithGoogle(token)

            result shouldBeEqualTo Result.Success("linked_uid")
        }

    @Test
    fun `GIVEN loginWithGoogle success WHEN loginWithGoogle THEN return UID`() =
        runTest {
            coEvery {
                auth.signInWith(any<Google>(), any(), any())
            } returns Unit
            every { auth.currentUserOrNull()?.id } returns "google_uid"

            val result = authService.loginWithGoogle("token")

            result shouldBeEqualTo Result.Success("google_uid")
        }

    @Test
    fun `GIVEN loginWithGoogle returns null WHEN loginWithGoogle THEN return GOOGLE_LOGIN_FAILED`() =
        runTest {
            coEvery {
                auth.signInWith(any<Google>(), any(), any())
            } returns Unit
            every { auth.currentUserOrNull()?.id } returns null

            val result = authService.loginWithGoogle("token")

            result shouldBeEqualTo Result.Error(LoginError.AuthService.GOOGLE_LOGIN_FAILED)
        }

    @Test
    fun `GIVEN user logged in WHEN isUserLogged THEN return true`() =
        runTest {
            val session = mockk<UserSession>()
            every { auth.currentSessionOrNull() } returns session

            val result = authService.isUserLogged()

            result shouldBe true
        }

    @Test
    fun `GIVEN no session WHEN isUserLogged THEN return false`() =
        runTest {
            every { auth.currentSessionOrNull() } returns null
            val sessionManager =
                mockk<SessionManager> {
                    coEvery { loadSession() } returns null
                }
            every { auth.sessionManager } returns sessionManager

            val result = authService.isUserLogged()

            result shouldBe false
        }

    @Test
    fun `GIVEN logout WHEN logout THEN signOut is called`() =
        runTest {
            coEvery { auth.signOut(any()) } returns Unit

            authService.logout()

            coVerifyOnce { auth.signOut(any()) }
        }

    @Test
    fun `GIVEN valid id token WHEN decodeEmailFromIdToken THEN return email`() {
        val token = buildIdToken("foo@test.com")
        val result =
            authService.let {
                val method = it.javaClass.getDeclaredMethod("decodeEmailFromIdToken", String::class.java)
                method.isAccessible = true
                method.invoke(it, token)
            }
        result shouldBeEqualTo "foo@test.com"
    }

    private fun buildIdToken(email: String): String {
        val header =
            android.util.Base64.encodeToString(
                "{}".toByteArray(),
                android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING,
            )
        val payload =
            android.util.Base64.encodeToString(
                """{"email":"$email"}""".toByteArray(),
                android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP or android.util.Base64.NO_PADDING,
            )
        return "$header.$payload.signature"
    }
}
