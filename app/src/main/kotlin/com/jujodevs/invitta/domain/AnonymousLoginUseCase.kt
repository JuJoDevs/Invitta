package com.jujodevs.invitta.domain

import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.domain.asEmptyDataResult
import com.jujodevs.invitta.library.authservice.api.AuthService

class AnonymousLoginUseCase(
    private val authService: AuthService,
) {
    suspend operator fun invoke(): Result<Unit, LoginError> {
        return if (!authService.isUserLogged()) {
            authService.loginAnonymously().asEmptyDataResult()
        } else {
            Result.Success(Unit)
        }
    }
}
