package com.jujodevs.invitta.core.domain

sealed interface DataError : Error {
    enum class RemoteDatabase : DataError {
        DATABASE,
        UNAUTHORIZED,
        NO_INTERNET,
        NO_SIGNED_IN_USER,
        API_NOT_AVAILABLE,
        TOO_MANY_REQUESTS,
        UNKNOWN,
    }
}
