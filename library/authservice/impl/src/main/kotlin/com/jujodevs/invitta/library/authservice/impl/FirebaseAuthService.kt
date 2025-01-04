package com.jujodevs.invitta.library.authservice.impl

import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.jujodevs.invitta.core.domain.Error
import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.authservice.api.AuthService
import com.jujodevs.invitta.library.logger.api.Logger
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthService(
    private val firebaseAuth: FirebaseAuth,
    private val logger: Logger,
) : AuthService {
    override suspend fun loginAnonymously(): Result<String, Error> {
        return try {
            val result =
                firebaseAuth.signInAnonymously()
                    .await().user?.uid
            result?.let {
                Result.Success(it)
            } ?: Result.Error(LoginError.AuthService.ANONYMOUS_LOGIN_FAILED)
        } catch (e: FirebaseException) {
            logger.e("FirebaseAuthException during anonymous login", e)
            Result.Error(LoginError.AuthService.ANONYMOUS_LOGIN_FAILED)
        } catch (e: FirebaseAuthException) {
            logger.e("FirebaseException during anonymous login", e)
            Result.Error(LoginError.AuthService.ANONYMOUS_LOGIN_FAILED)
        }
    }

    override suspend fun loginAndLinkWithGoogle(tokenId: String): Result<String, Error> {
        val currentAnonymousUid = firebaseAuth.currentUser?.uid
        val credential = GoogleAuthProvider.getCredential(tokenId, null)
        return if (currentAnonymousUid == null) {
            Result.Error(LoginError.AuthService.NO_USER_LOGGED)
        } else {
            try {
                linkAccountWithCredential(credential)
            } catch (e: FirebaseAuthUserCollisionException) {
                logger.w("Collision detected during linking", e)
                Result.Error(LoginError.AuthService.AUTH_USER_COLLISION)
            }
        }
    }

    override suspend fun loginWithGoogle(tokenId: String): Result<String, Error> {
        val credential = GoogleAuthProvider.getCredential(tokenId, null)
        val result = completeRegisterWithCredential(credential)
        return result?.let {
            Result.Success(it)
        } ?: Result.Error(LoginError.AuthService.GOOGLE_LOGIN_FAILED)
    }

    private suspend fun linkAccountWithCredential(
        credential: AuthCredential,
    ): Result<String, Error> {
        return suspendCancellableCoroutine { continuation ->
            firebaseAuth.currentUser?.linkWithCredential(credential)
                ?.addOnSuccessListener { result ->
                    continuation.resume(
                        result.user?.uid?.let { Result.Success(it) }
                            ?: Result.Error(LoginError.AuthService.LINKING_FAILED),
                    )
                }
                ?.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                } ?: continuation.resume(Result.Error(LoginError.AuthService.NO_USER_LOGGED))
        }
    }

    private suspend fun completeRegisterWithCredential(credential: AuthCredential): String? {
        return suspendCancellableCoroutine { cancellableContinuation ->
            firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener {
                    cancellableContinuation.resume(it.user?.uid)
                }
                .addOnFailureListener {
                    cancellableContinuation.resume(null)
                }
        }
    }

    override fun isUserLogged(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}
