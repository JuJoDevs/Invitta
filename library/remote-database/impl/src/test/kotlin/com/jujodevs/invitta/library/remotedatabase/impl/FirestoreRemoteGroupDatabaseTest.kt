package com.jujodevs.invitta.library.remotedatabase.impl

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.testing.verifyOnce
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.GroupDto
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.UpdateGroupDto
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FirestoreRemoteGroupDatabaseTest {
    private val db = mockk<FirebaseFirestore>()
    private val eventsCollection = mockk<CollectionReference>()
    private val groupsCollection = mockk<CollectionReference>()
    private val documentReference = mockk<DocumentReference>()

    private lateinit var remoteGroupDatabase: FirestoreRemoteGroupDatabase

    @BeforeEach
    fun setUp() {
        every { db.collection(EVENTS_COLLECTION) } returns eventsCollection
        every { eventsCollection.document(any()) } returns documentReference
        every { documentReference.collection(GROUPS_COLLECTION) } returns groupsCollection

        remoteGroupDatabase = FirestoreRemoteGroupDatabase(db)
    }

    @Test
    fun `GIVEN group data WHEN addGroup THEN calls addListeners`() =
        runTest {
            val group = GroupDto(name = "Group Name")
            val eventId = "event123"
            val groupId = "group123"
            val onResult = mockk<(Result<String, DataError>) -> Unit>()
            val task = mockk<Task<DocumentReference>>()
            val documentReference = mockk<DocumentReference>()
            val successListenerSlot = slot<OnSuccessListener<DocumentReference>>()
            val failureListenerSlot = slot<OnFailureListener>()
            val exception = RuntimeException("Simulated Failure")
            every { groupsCollection.add(group) } returns task
            every { task.addOnSuccessListener(capture(successListenerSlot)) } returns task
            every { task.addOnFailureListener(capture(failureListenerSlot)) } returns task
            every { documentReference.id } returns groupId
            every { onResult(any()) } just Runs

            remoteGroupDatabase.addGroup(group, eventId, onResult)

            successListenerSlot.captured.onSuccess(documentReference)
            verifyOnce { onResult(Result.Success(groupId)) }
            failureListenerSlot.captured.onFailure(exception)
            verifyOnce { onResult(Result.Error(DataError.RemoteDatabase.UNKNOWN)) }
        }

    @Test
    fun `GIVEN group data WHEN setGroup THEN calls addListeners`() =
        runTest {
            val group = UpdateGroupDto(name = "Group Name")
            val eventId = "event123"
            val groupId = "group123"
            val onResult = mockk<(Result<Unit, DataError>) -> Unit>()
            val task = mockk<Task<Void>>()
            val successListenerSlot = slot<OnSuccessListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()
            val exception = RuntimeException("Simulated Failure")
            every { groupsCollection.document(groupId) } returns documentReference
            every { documentReference.set(group, SetOptions.merge()) } returns task
            every { task.addOnSuccessListener(capture(successListenerSlot)) } returns task
            every { task.addOnFailureListener(capture(failureListenerSlot)) } returns task
            every { onResult(any()) } returns Unit

            remoteGroupDatabase.setGroup(group, eventId, groupId, onResult)
            successListenerSlot.captured.onSuccess(null)
            failureListenerSlot.captured.onFailure(exception)

            verifyOnce { onResult(Result.Success(Unit)) }
            verifyOnce { onResult(Result.Error(DataError.RemoteDatabase.UNKNOWN)) }
        }

    @Test
    fun `GIVEN group id WHEN deleteGroup THEN calls addListeners`() =
        runTest {
            val eventId = "event123"
            val groupId = "group123"
            val onResult = mockk<(Result<Unit, DataError>) -> Unit>()
            val task = mockk<Task<Void>>()
            val successListenerSlot = slot<OnSuccessListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()
            val exception = RuntimeException("Simulated Failure")
            every { groupsCollection.document(groupId) } returns documentReference
            every { documentReference.delete() } returns task
            every { task.addOnSuccessListener(capture(successListenerSlot)) } returns task
            every { task.addOnFailureListener(capture(failureListenerSlot)) } returns task
            every { onResult(any()) } returns Unit

            remoteGroupDatabase.deleteGroup(eventId, groupId, onResult)
            successListenerSlot.captured.onSuccess(null)
            failureListenerSlot.captured.onFailure(exception)

            verifyOnce { onResult(Result.Success(Unit)) }
            verifyOnce { onResult(Result.Error(DataError.RemoteDatabase.UNKNOWN)) }
        }
}
