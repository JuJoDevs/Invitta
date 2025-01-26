package com.jujodevs.invitta.library.remotedatabase.impl

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.testing.verifyOnce
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.MemberDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FirestoreRemoteMemberDatabaseTest {
    private val db = mockk<FirebaseFirestore>()
    private val eventsCollection = mockk<CollectionReference>()
    private val groupsCollection = mockk<CollectionReference>()
    private val nucleusCollection = mockk<CollectionReference>()
    private val membersCollection = mockk<CollectionReference>()
    private val documentReference = mockk<DocumentReference>()

    private lateinit var remoteMemberDatabase: FirestoreRemoteMemberDatabase

    @BeforeEach
    fun setUp() {
        every { db.collection(EVENTS_COLLECTION) } returns eventsCollection
        every { eventsCollection.document(any()) } returns documentReference
        every { documentReference.collection(GROUPS_COLLECTION) } returns groupsCollection
        every { groupsCollection.document(any()) } returns documentReference
        every { documentReference.collection(NUCLEUS_COLLECTION) } returns nucleusCollection
        every { nucleusCollection.document(any()) } returns documentReference
        every { documentReference.collection(MEMBERS_COLLECTION) } returns membersCollection

        remoteMemberDatabase = FirestoreRemoteMemberDatabase(db)
    }

    @Test
    fun `GIVEN member data WHEN addMember THEN adds member and updates references`() =
        runTest {
            val member =
                MemberDto(
                    name = "John Doe",
                    email = "o0NlR@example.com",
                )
            val eventId = "event123"
            val groupId = "group123"
            val nucleusId = "nucleus123"
            val newMemberId = "newMember123"
            val exception = RuntimeException("Simulated Failure")
            val onResult = mockk<(Result<String, DataError>) -> Unit>()
            val task = mockk<Task<DocumentReference>>()
            val taskVoid = mockk<Task<Void>>()
            val successListenerSlot = slot<OnSuccessListener<DocumentReference>>()
            val failureListenerSlot = slot<OnFailureListener>()
            every { membersCollection.document() } returns documentReference
            every { documentReference.id } returns newMemberId
            every { membersCollection.add(member) } returns task
            every { documentReference.update(AUTHORIZED_MEMBER_IDS_FIELD, any()) } returns taskVoid
            every { task.addOnSuccessListener(capture(successListenerSlot)) } returns task
            every { task.addOnFailureListener(capture(failureListenerSlot)) } returns task
            every { onResult(any()) } returns Unit

            remoteMemberDatabase.addMember(member, eventId, groupId, nucleusId, onResult)
            successListenerSlot.captured.onSuccess(documentReference)
            failureListenerSlot.captured.onFailure(exception)

            verifyOnce { onResult(Result.Success(newMemberId)) }
            verifyOnce { onResult(Result.Error(DataError.RemoteDatabase.UNKNOWN)) }
            verify(exactly = 3) { documentReference.update(AUTHORIZED_MEMBER_IDS_FIELD, any()) }
        }

    @Test
    fun `GIVEN member data WHEN setMember THEN updates member`() =
        runTest {
            val member =
                MemberDto(
                    name = "John Doe",
                    email = "o0NlR@example.com",
                )
            val eventId = "event123"
            val groupId = "group123"
            val nucleusId = "nucleus123"
            val memberId = "existingMember123"
            val onResult = mockk<(Result<Unit, DataError>) -> Unit>()
            val task = mockk<Task<Void>>()
            val successListenerSlot = slot<OnSuccessListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()
            val exception = RuntimeException("Simulated Failure")
            every { membersCollection.document(memberId) } returns documentReference
            every { documentReference.set(member) } returns task
            every { task.addOnSuccessListener(capture(successListenerSlot)) } returns task
            every { task.addOnFailureListener(capture(failureListenerSlot)) } returns task
            every { onResult(any()) } returns Unit
            every { onResult(any()) } returns Unit

            remoteMemberDatabase.setMember(member, eventId, groupId, nucleusId, memberId, onResult)
            successListenerSlot.captured.onSuccess(null)
            failureListenerSlot.captured.onFailure(exception)

            verifyOnce { onResult(Result.Success(Unit)) }
            verifyOnce { onResult(Result.Error(DataError.RemoteDatabase.UNKNOWN)) }
        }

    @Test
    fun `GIVEN memberId WHEN deleteMember THEN deletes member and updates references`() =
        runTest {
            val eventId = "event123"
            val groupId = "group123"
            val nucleusId = "nucleus123"
            val memberId = "member123"
            val exception = RuntimeException("Simulated Failure")
            val onResult = mockk<(Result<Unit, DataError>) -> Unit>()
            val task = mockk<Task<Void>>()
            val successListenerSlot = slot<OnSuccessListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()
            every { membersCollection.document(memberId) } returns documentReference
            every { documentReference.delete() } returns task
            every { documentReference.update(AUTHORIZED_MEMBER_IDS_FIELD, any()) } returns task
            every { task.addOnSuccessListener(capture(successListenerSlot)) } returns task
            every { task.addOnFailureListener(capture(failureListenerSlot)) } returns task
            every { onResult(any()) } returns Unit

            remoteMemberDatabase.deleteMember(eventId, groupId, nucleusId, memberId, onResult)
            successListenerSlot.captured.onSuccess(null)
            failureListenerSlot.captured.onFailure(exception)

            verifyOnce { onResult(Result.Success(Unit)) }
            verifyOnce { onResult(Result.Error(DataError.RemoteDatabase.UNKNOWN)) }
            verify(exactly = 3) { documentReference.update(AUTHORIZED_MEMBER_IDS_FIELD, any()) }
        }
}
