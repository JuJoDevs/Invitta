package com.jujodevs.invitta.library.authservice.impl

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.testing.rule.CoroutineTestRule
import com.jujodevs.invitta.core.testing.verifyOnce
import com.jujodevs.invitta.library.logger.api.Logger
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FirebaseAuthServiceTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var authService: FirebaseAuthService
    private val firebaseAuth = mockk<FirebaseAuth>()
    private val logger = mockk<Logger>()

    @Before
    fun setUp() {
        authService = FirebaseAuthService(firebaseAuth, logger)
        every { logger.e(any()) } returns Unit
        every { logger.w(any(), any(), any()) } returns Unit
    }

    @Test
    fun `GIVEN anonymous login succeeds WHEN loginAnonymously THEN return user UID`() =
        runTest {
            val expectedUid = "test_uid"
            val mockTask = mockk<Task<AuthResult>>()
            val mockResult =
                mockk<AuthResult> {
                    every { user?.uid } returns expectedUid
                }
            every { firebaseAuth.signInAnonymously() } returns mockTask
            every { mockTask.isComplete } returns true
            every { mockTask.isSuccessful } returns true
            every { mockTask.isCanceled } returns false
            every { mockTask.result } returns mockResult
            every { mockTask.exception } returns null
            every { mockTask.addOnSuccessListener(any()) } answers {
                val listener = arg<OnSuccessListener<AuthResult>>(0)
                listener.onSuccess(mockResult)
                mockTask
            }
            every { mockTask.addOnFailureListener(any()) } returns mockTask

            val result = authService.loginAnonymously()

            result shouldBeEqualTo Result.Success(expectedUid)
        }

    @Test
    fun `GIVEN anonymous login fails WHEN loginAnonymously THEN return ANONYMOUS_LOGIN_FAILED`() =
        runTest {
            val mockTask = mockk<Task<AuthResult>>()
            val mockResult =
                mockk<AuthResult> {
                    every { user?.uid } returns null
                }
            every { firebaseAuth.signInAnonymously() } returns mockTask
            every { mockTask.isComplete } returns true
            every { mockTask.isSuccessful } returns true
            every { mockTask.isCanceled } returns false
            every { mockTask.result } returns mockResult
            every { mockTask.exception } returns null
            every { mockTask.addOnSuccessListener(any()) } returns mockTask
            every { mockTask.addOnFailureListener(any()) } answers {
                val listener = arg<OnFailureListener>(0)
                listener.onFailure(Exception("Firebase error"))
                mockTask
            }

            val result = authService.loginAnonymously()

            result shouldBeEqualTo Result.Error(LoginError.AuthService.ANONYMOUS_LOGIN_FAILED)
        }

    @Test
    fun `GIVEN no user logged WHEN loginAndLinkWithGoogle THEN return NO_USER_LOGGED`() =
        runTest {
            val tokenId = "test_token"
            every { firebaseAuth.currentUser } returns null

            val result = authService.loginAndLinkWithGoogle(tokenId)

            result shouldBeEqualTo Result.Error(LoginError.AuthService.NO_USER_LOGGED)
        }

    @Test
    fun `GIVEN user logged WHEN loginAndLinkWithGoogle THEN link account and return UID`() =
        runTest {
            val tokenId = "test_token"
            val currentUid = "anonymous_uid"
            val linkedUid = "linked_uid"
            val mockCredential = mockk<AuthCredential>()
            val mockTask = mockk<Task<AuthResult>>()
            val mockResult =
                mockk<AuthResult> {
                    every { user?.uid } returns linkedUid
                }

            mockkStatic(GoogleAuthProvider::class)
            every { GoogleAuthProvider.getCredential(tokenId, null) } returns mockCredential
            every { firebaseAuth.currentUser?.uid } returns currentUid
            every { firebaseAuth.currentUser?.linkWithCredential(mockCredential) } returns mockTask

            every { mockTask.addOnSuccessListener(any()) } answers {
                val listener = arg<OnSuccessListener<AuthResult>>(0)
                listener.onSuccess(mockResult)
                mockTask
            }
            every { mockTask.addOnFailureListener(any()) } returns mockTask

            val result = authService.loginAndLinkWithGoogle(tokenId)

            result shouldBeEqualTo Result.Success(linkedUid)
        }

    @Test
    fun `GIVEN user collision WHEN loginAndLinkWithGoogle THEN return AUTH_USER_COLLISION`() =
        runTest {
            val tokenId = "test_token"
            val currentUid = "anonymous_uid"
            val mockCredential = mockk<AuthCredential>()
            val mockTask = mockk<Task<AuthResult>>()
            mockkStatic(GoogleAuthProvider::class)
            every { GoogleAuthProvider.getCredential(tokenId, null) } returns mockCredential
            every { firebaseAuth.currentUser?.uid } returns currentUid
            every { firebaseAuth.currentUser?.linkWithCredential(mockCredential) } returns mockTask
            every { mockTask.addOnSuccessListener(any()) } returns mockTask
            every { mockTask.addOnFailureListener(any()) } answers {
                val listener = arg<OnFailureListener>(0)
                listener.onFailure(FirebaseAuthUserCollisionException("Error", "Collision detected"))
                mockTask
            }

            val result = authService.loginAndLinkWithGoogle(tokenId)

            result shouldBeEqualTo Result.Error(LoginError.AuthService.AUTH_USER_COLLISION)
        }

    @Test
    fun `GIVEN google login succeeds WHEN loginWithGoogle THEN return user UID`() =
        runTest {
            val tokenId = "test_token_id"
            val expectedUid = "google_uid"
            val credential = mockk<AuthCredential>()
            val mockTask = mockk<Task<AuthResult>>()
            val mockResult =
                mockk<AuthResult> {
                    every { user?.uid } returns expectedUid
                }
            mockkStatic(GoogleAuthProvider::class)
            every { GoogleAuthProvider.getCredential(tokenId, null) } returns credential
            every { firebaseAuth.signInWithCredential(credential) } returns mockTask
            every { mockTask.addOnSuccessListener(any()) } answers {
                val listener = arg<OnSuccessListener<AuthResult>>(0)
                listener.onSuccess(mockResult)
                mockTask
            }
            every { mockTask.addOnFailureListener(any()) } returns mockTask

            val result = authService.loginWithGoogle(tokenId)

            result shouldBeEqualTo Result.Success(expectedUid)
        }

    @Test
    fun `GIVEN google login fails WHEN loginWithGoogle THEN return GOOGLE_LOGIN_FAILED`() =
        runTest {
            val tokenId = "invalid_token"
            val credential = mockk<AuthCredential>()
            val mockTask = mockk<Task<AuthResult>>()
            mockkStatic(GoogleAuthProvider::class)
            every { GoogleAuthProvider.getCredential(tokenId, null) } returns credential
            every { firebaseAuth.signInWithCredential(credential) } returns mockTask
            every { mockTask.addOnSuccessListener(any()) } returns mockTask
            every { mockTask.addOnFailureListener(any()) } answers {
                val listener = arg<OnFailureListener>(0)
                listener.onFailure(Exception("Firebase error"))
                mockTask
            }

            val result = authService.loginWithGoogle(tokenId)

            result shouldBeEqualTo Result.Error(LoginError.AuthService.GOOGLE_LOGIN_FAILED)
        }

    @Test
    fun `GIVEN user is logged in WHEN isUserLogged THEN return true`() {
        val mockUser = mockk<FirebaseUser>()
        every { firebaseAuth.currentUser } returns mockUser

        val result = authService.isUserLogged()

        result shouldBe true
    }

    @Test
    fun `GIVEN user is not logged in WHEN isUserLogged THEN return false`() {
        every { firebaseAuth.currentUser } returns null

        val result = authService.isUserLogged()

        result shouldBe false
    }

    @Test
    fun `GIVEN logout is called WHEN logout THEN firebaseAuth signOut is invoked`() {
        every { firebaseAuth.signOut() } returns Unit

        authService.logout()

        verifyOnce { firebaseAuth.signOut() }
    }
}
