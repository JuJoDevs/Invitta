package com.jujodevs.invitta.core.domain

sealed interface LoginError : Error {
    enum class AuthService : LoginError {
        ANONYMOUS_LOGIN_FAILED,
        GOOGLE_LOGIN_FAILED,
        NO_USER_LOGGED,
        LINKING_FAILED,
        AUTH_USER_COLLISION,
        REST_EXCEPTION,
        AUTH_REST_EXCEPTION,
        AUTH_HTTP_REQUEST_EXCEPTION,
        AUTH_HTTP_REQUEST_TIMEOUT_EXCEPTION,
        UNKNOWN_ERROR,
    }

    enum class GoogleAuth : LoginError {
        GOOGLE_SIGN_IN_FAILED,
        WEB_CLIENT_ID_NOT_FOUND,
        UNEXPECTED_CREDENTIAL_TYPE,
        EMAIL_CLAIM_NOT_FOUND,
        NO_ACTIVITY,
    }
}
