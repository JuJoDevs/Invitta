package com.jujodevs.invitta.core.domain

interface LoginError : Error {
    enum class AuthService : LoginError {
        ANONYMOUS_LOGIN_FAILED,
        GOOGLE_LOGIN_FAILED,
    }

    enum class GoogleAuth : LoginError {
        GOOGLE_SIGN_IN_FAILED,
        WEB_CLIENT_ID_NOT_FOUND,
        UNEXPECTED_CREDENTIAL_TYPE,
        NO_ACTIVITY,
    }
}
