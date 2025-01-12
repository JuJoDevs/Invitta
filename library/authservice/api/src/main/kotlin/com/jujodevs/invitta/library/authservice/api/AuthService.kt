package com.jujodevs.invitta.library.authservice.api

import com.jujodevs.invitta.core.domain.Error
import com.jujodevs.invitta.core.domain.Result

interface AuthService {
    suspend fun loginAnonymously(): Result<String, Error>
    suspend fun loginAndLinkWithGoogle(tokenId: String): Result<String, Error>
    suspend fun loginWithGoogle(tokenId: String): Result<String, Error>
    fun isUserLogged(): Boolean
    fun getCurrentUserId(): String
    fun logout()
}
