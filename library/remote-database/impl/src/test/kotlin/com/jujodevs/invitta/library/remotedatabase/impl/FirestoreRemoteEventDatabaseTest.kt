package com.jujodevs.invitta.library.remotedatabase.impl

import app.cash.turbine.test
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.testing.verifyOnce
import com.jujodevs.invitta.library.logger.api.Logger
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.EventDto
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.UpdateEventDto
import com.jujodevs.invitta.library.remotedatabase.api.model.response.EventResponse
import com.jujodevs.invitta.library.remotedatabase.api.model.response.GroupResponse
import com.jujodevs.invitta.library.remotedatabase.api.model.response.MemberResponse
import com.jujodevs.invitta.library.remotedatabase.api.model.response.NucleusResponse
import com.jujodevs.invitta.library.remotedatabase.impl.mapper.toFirebaseEventDto
import com.jujodevs.invitta.library.remotedatabase.impl.model.FirebaseEventResponse
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FirestoreRemoteEventDatabaseTest {
    private val db = mockk<FirebaseFirestore>()
    private val logger = mockk<Logger>()
    private val eventsCollection = mockk<CollectionReference>()
    private val documentReference = mockk<DocumentReference>()

    private lateinit var remoteEventDatabase: FirestoreRemoteEventDatabase

    @BeforeEach
    fun setUp() {
        mockkStatic("com.google.firebase.firestore.FirestoreKt")
        every { db.collection(EVENTS_COLLECTION) } returns eventsCollection
        every { eventsCollection.document(any()) } returns documentReference
        every { logger.e(any(), any()) } returns Unit
        remoteEventDatabase =
            FirestoreRemoteEventDatabase(
                db,
                logger,
            )
    }

    @Test
    fun `GIVEN empty uid WHEN getEvent THEN returns empty EventResponse`() =
        runTest {
            remoteEventDatabase.getEvent("")
                .test {
                    awaitItem() shouldBeEqualTo EventResponse()
                    awaitComplete()
                }
        }

    @Test
    fun `GIVEN valid eventId WHEN getEvent THEN returns EventResponse`() =
        runTest {
            val eventId = "event1"
            val eventName = "Event 1"
            val groupId = "group1"
            val groupName = "Group 1"
            val nucleusId = "nucleus1"
            val nucleusName = "Nucleus 1"
            val memberId = "member1"
            val memberName = "Member 1"
            val eventSnapshot =
                mockk<DocumentSnapshot> {
                    every { toObject<FirebaseEventResponse>() } returns
                        FirebaseEventResponse(
                            id = eventId,
                            name = eventName,
                        )
                    every { id } returns eventId
                    every { exists() } returns true
                }
            val groupSnapshot =
                mockk<QueryDocumentSnapshot> {
                    every { toObject<GroupResponse>() } returns
                        GroupResponse(
                            id = groupId,
                            name = groupName,
                        )
                    every { id } returns groupId
                }
            val groupsQuerySnapshot =
                mockk<QuerySnapshot> {
                    every { documents } returns listOf(groupSnapshot)
                }
            val nucleusSnapshot =
                mockk<QueryDocumentSnapshot> {
                    every { toObject<NucleusResponse>() } returns
                        NucleusResponse(
                            id = nucleusId,
                            name = nucleusName,
                        )
                    every { id } returns nucleusId
                }
            val nucleusQuerySnapshot =
                mockk<QuerySnapshot> {
                    every { documents } returns listOf(nucleusSnapshot)
                }
            val memberSnapshot =
                mockk<QueryDocumentSnapshot> {
                    every { toObject<MemberResponse>() } returns
                        MemberResponse(
                            id = memberId,
                            name = memberName,
                        )
                    every { id } returns memberId
                }
            val membersQuerySnapshot =
                mockk<QuerySnapshot> {
                    every { documents } returns listOf(memberSnapshot)
                }
            every {
                eventsCollection.document(eventId)
                    .snapshots()
            } returns flowOf(eventSnapshot)
            every {
                eventsCollection.document(eventId)
                    .collection(GROUPS_COLLECTION)
                    .snapshots()
            } returns flowOf(groupsQuerySnapshot)
            every {
                eventsCollection.document(eventId)
                    .collection(GROUPS_COLLECTION)
                    .document(groupId)
                    .collection(NUCLEUS_COLLECTION)
                    .snapshots()
            } returns flowOf(nucleusQuerySnapshot)
            every {
                eventsCollection.document(eventId)
                    .collection(GROUPS_COLLECTION)
                    .document(groupId)
                    .collection(NUCLEUS_COLLECTION)
                    .document(nucleusId)
                    .collection(MEMBERS_COLLECTION)
                    .snapshots()
            } returns flowOf(membersQuerySnapshot)

            remoteEventDatabase.getEvent(eventId)
                .test {
                    awaitItem() shouldBeEqualTo
                        EventResponse(
                            id = eventId,
                            name = eventName,
                            groups =
                                listOf(
                                    GroupResponse(
                                        id = groupId,
                                        name = groupName,
                                        nucleus =
                                            listOf(
                                                NucleusResponse(
                                                    id = nucleusId,
                                                    name = nucleusName,
                                                    members =
                                                        listOf(
                                                            MemberResponse(
                                                                id = memberId,
                                                                name = memberName,
                                                            ),
                                                        ),
                                                ),
                                            ),
                                    ),
                                ),
                        )
                    awaitComplete()
                }
        }

    @Test
    fun `GIVEN exception WHEN getEvent THEN logs error and returns empty EventResponse`() =
        runTest {
            val eventId = "event123"
            val exception = RuntimeException("Simulated Error")
            every { documentReference.snapshots() } answers { flow { throw exception } }
            every {
                logger.e(
                    any(),
                    exception,
                )
            } returns Unit

            remoteEventDatabase.getEvent(eventId)
                .test {
                    awaitItem() shouldBeEqualTo EventResponse()
                    awaitComplete()
                }
            verifyOnce {
                logger.e(
                    "Simulated Error",
                    exception,
                )
            }
        }

    @Test
    fun `GIVEN event data WHEN addEvent THEN calls addListeners`() {
        val eventDto =
            EventDto(
                organizerId = "organizer123",
                name = "Sample Event",
                dateSeconds = 1672531200,
                description = "This is a test event",
            )
        val eventId = "event123"
        val exception = RuntimeException("Simulated Failure")
        val onResult: (Result<String, DataError>) -> Unit = mockk()
        val task = mockk<Task<DocumentReference>>()
        val documentReference = mockk<DocumentReference>()
        val successListenerSlot = slot<OnSuccessListener<DocumentReference>>()
        val failureListenerSlot = slot<OnFailureListener>()
        every { onResult(any()) } just Runs
        every { eventsCollection.add(eventDto) } returns task
        every { task.addOnSuccessListener(capture(successListenerSlot)) } returns task
        every { task.addOnFailureListener(capture(failureListenerSlot)) } returns task
        every { onResult(any()) } returns Unit
        every { documentReference.id } returns eventId

        remoteEventDatabase.addEvent(eventDto, onResult)

        successListenerSlot.captured.onSuccess(documentReference)
        verifyOnce { onResult(Result.Success(eventId)) }
        failureListenerSlot.captured.onFailure(exception)
        verifyOnce { onResult(Result.Error(DataError.RemoteDatabase.UNKNOWN)) }
    }

    @Test
    fun `GIVEN event data WHEN setEvent THEN calls addListeners`() {
        val eventDto =
            UpdateEventDto(
                organizerId = "organizer123",
                name = "Sample Event",
                dateSeconds = 1672531200,
                description = "This is a test event",
            )
        val onResult = mockk<(Result<Unit, DataError>) -> Unit>()
        val eventId = "event123"
        val exception = RuntimeException("Simulated Failure")
        val task = mockk<Task<Void>>()
        val successListenerSlot = slot<OnSuccessListener<Void>>()
        val failureListenerSlot = slot<OnFailureListener>()
        every {
            eventsCollection.document(eventId)
                .set(eventDto.toFirebaseEventDto(), SetOptions.merge())
        } returns task
        every { task.addOnSuccessListener(capture(successListenerSlot)) } returns task
        every { task.addOnFailureListener(capture(failureListenerSlot)) } returns task
        every { onResult(any()) } returns Unit

        remoteEventDatabase.setEvent(
            eventDto,
            eventId,
            onResult,
        )
        successListenerSlot.captured.onSuccess(null)
        failureListenerSlot.captured.onFailure(exception)

        verifyOnce { onResult(Result.Success(Unit)) }
        verifyOnce { onResult(Result.Error(DataError.RemoteDatabase.UNKNOWN)) }
    }

    @Test
    fun `GIVEN eventId WHEN deleteEvent THEN calls addListeners`() {
        val eventId = "event123"
        val onResult = mockk<(Result<Unit, DataError>) -> Unit>()
        val task = mockk<Task<Void>>()
        val successListenerSlot = slot<OnSuccessListener<Void>>()
        val failureListenerSlot = slot<OnFailureListener>()

        every {
            eventsCollection.document(eventId)
                .delete()
        } returns task
        every { task.addOnSuccessListener(capture(successListenerSlot)) } returns task
        every { task.addOnFailureListener(capture(failureListenerSlot)) } returns task
        every { onResult(any()) } returns Unit

        remoteEventDatabase.deleteEvent(
            eventId,
            onResult,
        )

        successListenerSlot.captured.onSuccess(null)
        verifyOnce { onResult(Result.Success(Unit)) }

        val exception = RuntimeException("Simulated Failure")
        failureListenerSlot.captured.onFailure(exception)
        verifyOnce { onResult(Result.Error(DataError.RemoteDatabase.UNKNOWN)) }
    }
}
