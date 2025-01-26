package com.jujodevs.invitta.library.authservice.api

import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.domain.Result

interface AuthService {
    suspend fun loginAnonymously(): Result<String, LoginError>
    suspend fun loginAndLinkWithGoogle(tokenId: String): Result<String, LoginError>
    suspend fun loginWithGoogle(tokenId: String): Result<String, LoginError>
    fun isUserLogged(): Boolean
    fun getCurrentUserId(): String
    fun logout()
}
