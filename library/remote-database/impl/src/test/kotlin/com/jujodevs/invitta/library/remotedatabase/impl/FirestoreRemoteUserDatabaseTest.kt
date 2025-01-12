package com.jujodevs.invitta.library.remotedatabase.impl

import app.cash.turbine.test
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.testing.verifyOnce
import com.jujodevs.invitta.library.logger.api.Logger
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.UserDto
import com.jujodevs.invitta.library.remotedatabase.api.model.response.UserResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FirestoreRemoteUserDatabaseTest {
    private val db = mockk<FirebaseFirestore>(relaxed = true)
    private val logger = mockk<Logger>(relaxed = true)
    private val usersCollection = mockk<CollectionReference>(relaxed = true)
    private val documentReference = mockk<DocumentReference>(relaxed = true)

    private lateinit var remoteUserDatabase: FirestoreRemoteUserDatabase

    @BeforeEach
    fun setUp() {
        mockkStatic("com.google.firebase.firestore.FirestoreKt")
        every { db.collection("users") } returns usersCollection
        every { usersCollection.document(any()) } returns documentReference
        remoteUserDatabase = FirestoreRemoteUserDatabase(db, logger)
    }

    @Test
    fun `GIVEN empty uid WHEN getUser THEN returns empty UserResponse`() =
        runTest {
            remoteUserDatabase.getUser("").test {
                awaitItem() shouldBeEqualTo UserResponse()
                awaitComplete()
            }
        }

    @Test
    fun `GIVEN valid uid WHEN getUser THEN returns UserResponse`() =
        runTest {
            val uid = "12345"
            val userResponse = UserResponse(id = uid, name = "John Doe")
            val documentSnapshot = mockk<DocumentSnapshot>()
            every { documentSnapshot.toObject<UserResponse>() } returns userResponse
            every { documentSnapshot.id } returns uid
            val snapshotsFlow = flowOf(documentSnapshot)
            every { documentReference.snapshots() } returns snapshotsFlow

            remoteUserDatabase.getUser(uid).test {
                awaitItem() shouldBeEqualTo userResponse.copy(id = uid)
                awaitComplete()
            }
        }

    @Test
    fun `GIVEN exception WHEN getUser THEN logs error and returns empty UserResponse`() =
        runTest {
            val uid = "12345"
            val expectedMsg = "Error getting Firestore document"
            val exception = RuntimeException(expectedMsg)
            every { documentReference.snapshots() } answers {
                flow {
                    throw exception
                }
            }
            every { logger.e(any(), exception) } returns Unit

            remoteUserDatabase.getUser(uid).test {
                awaitItem() shouldBeEqualTo UserResponse()
                awaitComplete()
            }
            verifyOnce { logger.e(expectedMsg, exception) }
        }

    @Test
    fun `GIVEN user data WHEN setUser THEN calls addListeners`() {
        val userDto = UserDto()
        val onResult = mockk<(Result<Unit, DataError>) -> Unit>()
        val userId = "user123"
        val exception = RuntimeException("Simulated Failure")
        val task = mockk<Task<Void>>()
        val successListenerSlot = slot<OnSuccessListener<Void>>()
        val failureListenerSlot = slot<OnFailureListener>()
        every { documentReference.set(userDto) } returns task
        every { task.addOnSuccessListener(capture(successListenerSlot)) } returns task
        every { task.addOnFailureListener(capture(failureListenerSlot)) } returns task
        every { onResult(any()) } returns Unit

        remoteUserDatabase.setUser(userDto, userId, onResult)
        successListenerSlot.captured.onSuccess(null)
        failureListenerSlot.captured.onFailure(exception)

        verifyOnce { onResult(Result.Success(Unit)) }
        verifyOnce {
            onResult(Result.Error(DataError.RemoteDatabase.UNKNOWN))
        }
    }

    @Test
    fun `GIVEN user id WHEN deleteUser THEN calls addListeners`() {
        val userId = "user123"
        val onResult = mockk<(Result<Unit, DataError>) -> Unit>()
        val exception = RuntimeException("Simulated Failure")
        val task = mockk<Task<Void>>()
        val successListenerSlot = slot<OnSuccessListener<Void>>()
        val failureListenerSlot = slot<OnFailureListener>()
        every { documentReference.delete() } returns task
        every { task.addOnSuccessListener(capture(successListenerSlot)) } returns task
        every { task.addOnFailureListener(capture(failureListenerSlot)) } returns task
        every { onResult(any()) } returns Unit

        remoteUserDatabase.deleteUser(userId, onResult)
        successListenerSlot.captured.onSuccess(null)
        failureListenerSlot.captured.onFailure(exception)

        verifyOnce { onResult(Result.Success(Unit)) }
        verifyOnce { onResult(Result.Error(DataError.RemoteDatabase.UNKNOWN)) }
    }
}
