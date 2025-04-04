package com.jujodevs.invitta.library.authservice.impl

import android.util.Base64
import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.authservice.api.AuthService
import com.jujodevs.invitta.library.logger.api.Logger
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.CancellationException
import org.json.JSONObject
import kotlin.uuid.ExperimentalUuidApi

class SupabaseAuthService(
    private val auth: Auth,
    private val logger: Logger,
) : AuthService {
    override suspend fun loginAnonymously(): Result<String, LoginError> {
        val result =
            safeCall {
                auth.signInAnonymously()
                auth.currentUserOrNull()
            }
        return when (result) {
            is Result.Error<LoginError> -> result
            is Result.Success<UserInfo?> -> {
                result.data?.let { userInfo ->
                    Result.Success(userInfo.id)
                } ?: Result.Error(LoginError.AuthService.ANONYMOUS_LOGIN_FAILED)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun loginAndLinkWithGoogle(tokenId: String): Result<String, LoginError> {
        val result =
            safeCall {
                decodeEmailFromIdToken(tokenId)
            }
        return when (result) {
            is Result.Error<LoginError> -> result
            is Result.Success<String?> -> {
                result.data?.let { decodedEmail ->
                    auth.updateUser {
                        email = decodedEmail
                    }
                    loginWithGoogle(tokenId)
                } ?: Result.Error(LoginError.GoogleAuth.EMAIL_CLAIM_NOT_FOUND)
            }
        }
    }

    override suspend fun loginWithGoogle(tokenId: String): Result<String, LoginError> {
        val result =
            safeCall {
                auth.signInWith(IDToken) {
                    idToken = tokenId
                    provider = Google
                }
                auth.currentUserOrNull()?.id
            }
        return when (result) {
            is Result.Error<LoginError> -> result
            is Result.Success<String?> -> {
                result.data?.let {
                    Result.Success(it)
                } ?: Result.Error(LoginError.AuthService.GOOGLE_LOGIN_FAILED)
            }
        }
    }

    override suspend fun isUserLogged(): Boolean {
        val result = safeCall { auth.currentSessionOrNull() ?: reloadSession() }
        return when (result) {
            is Result.Error<LoginError> -> false
            is Result.Success<UserSession?> -> result.data != null
        }
    }

    private suspend fun reloadSession(): UserSession? {
        return with(auth) {
            sessionManager.loadSession()?.let { userSession ->
                try {
                    retrieveUser(userSession.accessToken)
                    refreshCurrentSession()
                    currentSessionOrNull()
                } catch (e: HttpRequestException) {
                    logger.e(e.message.orEmpty(), e, SupabaseAuthService::class.simpleName)
                    userSession
                }
            }
        }
    }

    override fun getCurrentUserId(): String {
        return auth.currentUserOrNull()?.id.orEmpty()
    }

    override suspend fun logout() {
        safeCall { auth.signOut() }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun decodeEmailFromIdToken(idToken: String): String? {
        return try {
            val parts = idToken.split(".")
            if (parts.size != ID_TOKEN_PARTS_SIZE) return null

            val payload =
                String(
                    Base64.decode(
                        parts[1],
                        Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP,
                    ),
                )
            JSONObject(payload).optString(
                EMAIL_CLAIM,
                "null",
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger.e(
                e.message.orEmpty(),
                e,
                SupabaseAuthService::class.simpleName,
            )
            null
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun <T> safeCall(block: suspend () -> T): Result<T, LoginError> {
        return try {
            Result.Success(block())
        } catch (e: RestException) {
            logger.e(e.message.orEmpty(), e, SupabaseAuthService::class.simpleName)
            Result.Error(LoginError.AuthService.REST_EXCEPTION)
        } catch (e: AuthRestException) {
            logger.e(e.message.orEmpty(), e, SupabaseAuthService::class.simpleName)
            Result.Error(LoginError.AuthService.AUTH_REST_EXCEPTION)
        } catch (e: HttpRequestTimeoutException) {
            logger.e(e.message.orEmpty(), e, SupabaseAuthService::class.simpleName)
            Result.Error(LoginError.AuthService.AUTH_HTTP_REQUEST_EXCEPTION)
        } catch (e: HttpRequestException) {
            logger.e(e.message.orEmpty(), e, SupabaseAuthService::class.simpleName)
            Result.Error(LoginError.AuthService.AUTH_HTTP_REQUEST_TIMEOUT_EXCEPTION)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger.e(e.message.orEmpty(), e, SupabaseAuthService::class.simpleName)
            Result.Error(LoginError.AuthService.UNKNOWN_ERROR)
        }
    }
}

private const val ID_TOKEN_PARTS_SIZE = 3
private const val EMAIL_CLAIM = "email"
