package com.jujodevs.invitta.library.googleauth.impl

import android.app.Activity
import androidx.core.bundle.Bundle
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.jujodevs.invitta.core.activityprovider.api.ActivityProvider
import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.testing.rule.CoroutineTestRule
import com.jujodevs.invitta.core.testing.verifyOnce
import com.jujodevs.invitta.library.logger.api.Logger
import com.jujodevs.invitta.library.remoteconfig.api.RemoteConfigName
import com.jujodevs.invitta.library.remoteconfig.api.RemoteConfigProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DefaultGoogleAuthTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var googleAuth: DefaultGoogleAuth
    private val remoteConfigProvider = mockk<RemoteConfigProvider>()
    private val credentialManager = mockk<CredentialManager>()
    private val activityProvider = mockk<ActivityProvider>()
    private val logger = mockk<Logger>()

    private val testDispatcher = coroutineTestRule.testDispatcher

    @Before
    fun setUp() {
        googleAuth =
            DefaultGoogleAuth(
                remoteConfigProvider = remoteConfigProvider,
                credentialManager = credentialManager,
                activityProvider = activityProvider,
                logger = logger,
                mainDispatcher = testDispatcher,
            )
    }

    @Test
    fun `GIVEN no activity WHEN login THEN return NO_ACTIVITY error`() =
        runTest {
            val expectedLogMsg = "No activity found"
            every { activityProvider.currentActivity() } returns null
            every { logger.w(expectedLogMsg) } returns Unit

            val result = googleAuth.login()

            result shouldBeEqualTo Result.Error(LoginError.GoogleAuth.NO_ACTIVITY)
            verifyOnce { logger.w(expectedLogMsg) }
        }

    @Test
    fun `GIVEN empty web client id WHEN login THEN return WEB_CLIENT_ID_NOT_FOUND error`() =
        runTest {
            val expectedLogMsg = "Google web client id not found"
            val mockActivity = mockk<Activity>()
            every { activityProvider.currentActivity() } returns mockActivity
            every { remoteConfigProvider.getStringParameter(RemoteConfigName.GOOGLE_WEB_CLIENT_ID) } returns ""
            every { logger.w(expectedLogMsg) } returns Unit

            val result = googleAuth.login()

            result shouldBeEqualTo Result.Error(LoginError.GoogleAuth.WEB_CLIENT_ID_NOT_FOUND)
            verifyOnce { logger.w(expectedLogMsg) }
        }

    @Test
    fun `GIVEN valid setup WHEN login THEN return success with id token`() =
        runTest {
            val mockActivity = mockk<Activity>()
            val webClientId = "test_client_id"
            val expectedToken = "test_id_token"
            val bundle =
                Bundle().apply {
                    putString(
                        "com.google.android.libraries.identity.googleid.BUNDLE_KEY_PROFILE_PICTURE_URI",
                        "https://lh3.googleusercontent.com/something",
                    )
                    putString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_DISPLAY_NAME", "Somebody")
                    putString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_PHONE_NUMBER", null)
                    putString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID", "somebody@gmail.com")
                    putString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_GIVEN_NAME", "First name")
                    putString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID_TOKEN", expectedToken)
                    putString("com.google.android.libraries.identity.googleid.BUNDLE_KEY_FAMILY_NAME", "Last name")
                }
            val mockResponse =
                mockk<GetCredentialResponse> {
                    every { credential } returns
                        CustomCredential(
                            type = GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL,
                            data = bundle,
                        )
                }
            every { activityProvider.currentActivity() } returns mockActivity
            every { remoteConfigProvider.getStringParameter(RemoteConfigName.GOOGLE_WEB_CLIENT_ID) } returns webClientId
            coEvery { credentialManager.getCredential(mockActivity, any<GetCredentialRequest>()) } returns mockResponse

            val result = googleAuth.login()

            result shouldBeEqualTo Result.Success(expectedToken)
        }

    @Test
    fun `GIVEN exception WHEN login THEN return GOOGLE_SIGN_IN_FAILED error`() =
        runTest {
            val mockActivity = mockk<Activity>()
            val webClientId = "test_client_id"
            val expectedLogMsg = "Google sign in failed"
            every { activityProvider.currentActivity() } returns mockActivity
            every { remoteConfigProvider.getStringParameter(RemoteConfigName.GOOGLE_WEB_CLIENT_ID) } returns webClientId
            coEvery { credentialManager.getCredential(mockActivity, any<GetCredentialRequest>()) } throws mockk<GetCredentialException>()
            every { logger.w(expectedLogMsg, any<GetCredentialException>()) } returns Unit

            val result = googleAuth.login()

            result shouldBeEqualTo Result.Error(LoginError.GoogleAuth.GOOGLE_SIGN_IN_FAILED)
            verifyOnce { logger.w(expectedLogMsg, any<GetCredentialException>()) }
        }

    @Test
    fun `GIVEN unexpected credential type WHEN login THEN return UNEXPECTED_CREDENTIAL_TYPE error`() =
        runTest {
            val mockActivity = mockk<Activity>()
            val webClientId = "test_client_id"
            val type = "unknown_type"
            val bundle = Bundle()
            val mockResponse =
                mockk<GetCredentialResponse> {
                    every { credential } returns
                        CustomCredential(
                            type = type,
                            data = bundle,
                        )
                }
            val expectedLogMsg = "Unexpected credential type: unknown_type"
            every { activityProvider.currentActivity() } returns mockActivity
            every { remoteConfigProvider.getStringParameter(RemoteConfigName.GOOGLE_WEB_CLIENT_ID) } returns webClientId
            coEvery { credentialManager.getCredential(mockActivity, any<GetCredentialRequest>()) } returns mockResponse
            every { logger.w(expectedLogMsg) } returns Unit

            val result = googleAuth.login()

            result shouldBeEqualTo Result.Error(LoginError.GoogleAuth.UNEXPECTED_CREDENTIAL_TYPE)
            verifyOnce { logger.w(expectedLogMsg) }
        }
}
