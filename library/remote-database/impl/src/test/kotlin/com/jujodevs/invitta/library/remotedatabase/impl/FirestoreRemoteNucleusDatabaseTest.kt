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
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.NucleusDto
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.UpdateNucleusDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FirestoreRemoteNucleusDatabaseTest {
    private val db = mockk<FirebaseFirestore>()
    private val eventsCollection = mockk<CollectionReference>()
    private val groupsCollection = mockk<CollectionReference>()
    private val nucleusCollection = mockk<CollectionReference>()
    private val documentReference = mockk<DocumentReference>()

    private lateinit var remoteNucleusDatabase: FirestoreRemoteNucleusDatabase

    @BeforeEach
    fun setUp() {
        every { db.collection(EVENTS_COLLECTION) } returns eventsCollection
        every { eventsCollection.document(any()) } returns documentReference
        every { documentReference.collection(GROUPS_COLLECTION) } returns groupsCollection
        every { groupsCollection.document(any()) } returns documentReference
        every { documentReference.collection(NUCLEUS_COLLECTION) } returns nucleusCollection

        remoteNucleusDatabase = FirestoreRemoteNucleusDatabase(db)
    }

    @Test
    fun `GIVEN nucleus data WHEN addNucleus THEN calls addListeners`() =
        runTest {
            val nucleusName = "Nucleus Name"
            val organizerId = "organizer123"
            val nucleus = NucleusDto(name = nucleusName, organizerId = organizerId)
            val eventId = "event123"
            val groupId = "group123"
            val nucleusId = "nucleus123"
            val onResult = mockk<(Result<String, DataError>) -> Unit>()
            val task = mockk<Task<DocumentReference>>()
            val documentReference = mockk<DocumentReference>()
            val successListenerSlot = slot<OnSuccessListener<DocumentReference>>()
            val failureListenerSlot = slot<OnFailureListener>()
            val exception = RuntimeException("Simulated Failure")
            every { nucleusCollection.add(nucleus) } returns task
            every { task.addOnSuccessListener(capture(successListenerSlot)) } returns task
            every { task.addOnFailureListener(capture(failureListenerSlot)) } returns task
            every { documentReference.id } returns nucleusId
            every { onResult(any()) } returns Unit

            remoteNucleusDatabase.addNucleus(nucleus, eventId, groupId, onResult)

            successListenerSlot.captured.onSuccess(documentReference)
            verifyOnce { onResult(Result.Success(nucleusId)) }
            failureListenerSlot.captured.onFailure(exception)
            verifyOnce { onResult(Result.Error(DataError.RemoteDatabase.UNKNOWN)) }
        }

    @Test
    fun `GIVEN nucleus data WHEN setNucleus THEN calls addListeners`() =
        runTest {
            val nucleus = UpdateNucleusDto(name = "Nucleus Name")
            val eventId = "event123"
            val groupId = "group123"
            val nucleusId = "nucleus123"
            val onResult = mockk<(Result<Unit, DataError>) -> Unit>()
            val task = mockk<Task<Void>>()
            val successListenerSlot = slot<OnSuccessListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()
            val exception = RuntimeException("Simulated Failure")
            every { nucleusCollection.document(nucleusId) } returns documentReference
            every { documentReference.set(nucleus, SetOptions.merge()) } returns task
            every { task.addOnSuccessListener(capture(successListenerSlot)) } returns task
            every { task.addOnFailureListener(capture(failureListenerSlot)) } returns task
            every { onResult(any()) } returns Unit

            remoteNucleusDatabase.setNucleus(nucleus, eventId, groupId, nucleusId, onResult)
            successListenerSlot.captured.onSuccess(null)
            failureListenerSlot.captured.onFailure(exception)

            verifyOnce { onResult(Result.Success(Unit)) }
            verifyOnce { onResult(Result.Error(DataError.RemoteDatabase.UNKNOWN)) }
        }

    @Test
    fun `GIVEN nucleus id WHEN deleteNucleus THEN calls addListeners`() =
        runTest {
            val eventId = "event123"
            val groupId = "group123"
            val nucleusId = "nucleus123"
            val onResult = mockk<(Result<Unit, DataError>) -> Unit>()
            val task = mockk<Task<Void>>()
            val successListenerSlot = slot<OnSuccessListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()
            val exception = RuntimeException("Simulated Failure")
            every { nucleusCollection.document(nucleusId) } returns documentReference
            every { documentReference.delete() } returns task
            every { task.addOnSuccessListener(capture(successListenerSlot)) } returns task
            every { task.addOnFailureListener(capture(failureListenerSlot)) } returns task
            every { onResult(any()) } returns Unit

            remoteNucleusDatabase.deleteNucleus(eventId, groupId, nucleusId, onResult)
            successListenerSlot.captured.onSuccess(null)
            failureListenerSlot.captured.onFailure(exception)

            verifyOnce { onResult(Result.Success(Unit)) }
            verifyOnce { onResult(Result.Error(DataError.RemoteDatabase.UNKNOWN)) }
        }
}
