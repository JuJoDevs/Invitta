package com.jujodevs.invitta.library.googleauth.impl

import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.jujodevs.invitta.core.activityprovider.api.ActivityProvider
import com.jujodevs.invitta.core.domain.Error
import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.googleauth.api.GoogleAuth
import com.jujodevs.invitta.library.logger.api.Logger
import com.jujodevs.invitta.library.remoteconfig.api.RemoteConfigName
import com.jujodevs.invitta.library.remoteconfig.api.RemoteConfigProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultGoogleAuth(
    private val remoteConfigProvider: RemoteConfigProvider,
    private val credentialManager: CredentialManager,
    private val activityProvider: ActivityProvider,
    private val logger: Logger,
    private val mainDispatcher: CoroutineDispatcher,
) : GoogleAuth {
    override suspend fun login(): Result<String, Error> {
        val activity = activityProvider.currentActivity()
        return if (activity == null) {
            logger.w("No activity found")
            Result.Error(LoginError.GoogleAuth.NO_ACTIVITY)
        } else {
            val webClientId =
                remoteConfigProvider.getStringParameter(
                    RemoteConfigName.GOOGLE_WEB_CLIENT_ID,
                )
            if (webClientId.isEmpty()) {
                logger.w("Google web client id not found")
                Result.Error(LoginError.GoogleAuth.WEB_CLIENT_ID_NOT_FOUND)
            } else {
                val googleIdOption =
                    GetGoogleIdOption.Builder()
                        .setServerClientId(webClientId)
                        .setFilterByAuthorizedAccounts(false)
                        .setAutoSelectEnabled(false)
                        .build()

                val request =
                    GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                try {
                    val result =
                        withContext(mainDispatcher) {
                            credentialManager.getCredential(activity, request)
                        }
                    handleGoogleSignIn(result)
                } catch (e: GetCredentialException) {
                    logger.w("Google sign in failed", e)
                    Result.Error(LoginError.GoogleAuth.GOOGLE_SIGN_IN_FAILED)
                }
            }
        }
    }

    private fun handleGoogleSignIn(result: GetCredentialResponse): Result<String, Error> {
        val credential = result.credential
        return if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            Result.Success(googleIdTokenCredential.idToken)
        } else {
            val errorString = "Unexpected credential type: ${credential.type}"
            logger.w(errorString)
            Result.Error(LoginError.GoogleAuth.UNEXPECTED_CREDENTIAL_TYPE)
        }
    }
}
