package com.jujodevs.invitta.core.domain

sealed interface LoginError : Error {
    enum class AuthService : LoginError {
        ANONYMOUS_LOGIN_FAILED,
        GOOGLE_LOGIN_FAILED,
        NO_USER_LOGGED,
        LINKING_FAILED,
        AUTH_USER_COLLISION,
    }

    enum class GoogleAuth : LoginError {
        GOOGLE_SIGN_IN_FAILED,
        WEB_CLIENT_ID_NOT_FOUND,
        UNEXPECTED_CREDENTIAL_TYPE,
        NO_ACTIVITY,
    }
}
