package com.jujodevs.invitta.library.remotedatabase.impl.mapper

import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.jujodevs.invitta.core.domain.DataError

fun FirebaseException.toError(): DataError {
    return when (this) {
        is FirebaseFirestoreException -> DataError.RemoteDatabase.DATABASE
        is FirebaseAuthException -> DataError.RemoteDatabase.UNAUTHORIZED
        is FirebaseNetworkException -> DataError.RemoteDatabase.NO_INTERNET
        is FirebaseApiNotAvailableException -> DataError.RemoteDatabase.API_NOT_AVAILABLE
        is FirebaseTooManyRequestsException -> DataError.RemoteDatabase.TOO_MANY_REQUESTS
        is FirebaseNoSignedInUserException -> DataError.RemoteDatabase.NO_SIGNED_IN_USER
        else -> DataError.RemoteDatabase.UNKNOWN
    }
}

fun Exception.toRemoteDatabaseError(): DataError {
    return if (this is FirebaseAuthException) {
        this.toError()
    } else {
        DataError.RemoteDatabase.UNKNOWN
    }
}
