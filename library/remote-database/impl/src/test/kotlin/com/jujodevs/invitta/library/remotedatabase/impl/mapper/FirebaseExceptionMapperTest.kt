package com.jujodevs.invitta.library.remotedatabase.impl.mapper

import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.jujodevs.invitta.core.domain.DataError
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class FirebaseExceptionMapperTest {
    @Test
    fun `GIVEN FirebaseFirestoreException WHEN toError THEN returns DATABASE error`() {
        val exception = mockk<FirebaseFirestoreException>()

        val result = exception.toError()

        result shouldBeEqualTo DataError.RemoteDatabase.DATABASE
    }

    @Test
    fun `GIVEN FirebaseAuthException WHEN toError THEN returns UNAUTHORIZED error`() {
        val exception = mockk<FirebaseAuthException>()

        val result = exception.toError()

        result shouldBeEqualTo DataError.RemoteDatabase.UNAUTHORIZED
    }

    @Test
    fun `GIVEN FirebaseNetworkException WHEN toError THEN returns NO_INTERNET error`() {
        val exception = mockk<FirebaseNetworkException>()

        val result = exception.toError()

        result shouldBeEqualTo DataError.RemoteDatabase.NO_INTERNET
    }

    @Test
    fun `GIVEN FirebaseApiNotAvailableException WHEN toError THEN returns API_NOT_AVAILABLE error`() {
        val exception = mockk<FirebaseApiNotAvailableException>()

        val result = exception.toError()

        result shouldBeEqualTo DataError.RemoteDatabase.API_NOT_AVAILABLE
    }

    @Test
    fun `GIVEN FirebaseTooManyRequestsException WHEN toError THEN returns TOO_MANY_REQUESTS error`() {
        val exception = mockk<FirebaseTooManyRequestsException>()

        val result = exception.toError()

        result shouldBeEqualTo DataError.RemoteDatabase.TOO_MANY_REQUESTS
    }

    @Test
    fun `GIVEN FirebaseNoSignedInUserException WHEN toError THEN returns NO_SIGNED_IN_USER error`() {
        val exception = mockk<FirebaseNoSignedInUserException>()

        val result = exception.toError()

        result shouldBeEqualTo DataError.RemoteDatabase.NO_SIGNED_IN_USER
    }

    @Test
    fun `GIVEN unknown FirebaseException WHEN toError THEN returns UNKNOWN error`() {
        val exception = mockk<FirebaseException>()

        val result = exception.toError()

        result shouldBeEqualTo DataError.RemoteDatabase.UNKNOWN
    }

    @Test
    fun `GIVEN FirebaseAuthException WHEN toRemoteDatabaseError THEN returns mapped error`() {
        val exception = mockk<FirebaseAuthException>()

        val result = exception.toRemoteDatabaseError()

        result shouldBeEqualTo DataError.RemoteDatabase.UNAUTHORIZED
    }

    @Test
    fun `GIVEN unknown Exception WHEN toRemoteDatabaseError THEN returns UNKNOWN error`() {
        val exception = mockk<Exception>()

        val result = exception.toRemoteDatabaseError()

        result shouldBeEqualTo DataError.RemoteDatabase.UNKNOWN
    }
}
