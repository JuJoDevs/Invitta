package com.jujodevs.invitta.library.remotedatabase.impl

import com.google.firebase.FirebaseException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.jujodevs.invitta.core.coroutines.combineAll
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.logger.api.Logger
import com.jujodevs.invitta.library.remotedatabase.api.RemoteEventDatabase
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.EventDto
import com.jujodevs.invitta.library.remotedatabase.api.model.response.EventResponse
import com.jujodevs.invitta.library.remotedatabase.api.model.response.GroupResponse
import com.jujodevs.invitta.library.remotedatabase.api.model.response.MemberResponse
import com.jujodevs.invitta.library.remotedatabase.api.model.response.NucleusResponse
import com.jujodevs.invitta.library.remotedatabase.impl.mapper.toError
import com.jujodevs.invitta.library.remotedatabase.impl.mapper.toEventResponse
import com.jujodevs.invitta.library.remotedatabase.impl.mapper.toFirebaseEventDto
import com.jujodevs.invitta.library.remotedatabase.impl.model.FirebaseEventResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await

class FirestoreRemoteEventDatabase(
    db: FirebaseFirestore,
    private val logger: Logger,
) : RemoteEventDatabase {
    private val eventsCollection = db.collection(EVENTS_COLLECTION)

    override fun getEvents(): Flow<List<EventResponse>> {
        return eventsCollection
            .orderBy(
                DATE_FIELD,
                Query.Direction.DESCENDING,
            )
            .snapshots()
            .map { qs ->
                qs.documents.map {
                    it.toObject<FirebaseEventResponse>()
                        ?.copy(id = it.id)
                        ?.toEventResponse()
                        ?: EventResponse()
                }
            }
            .catch {
                logger.e(
                    it.message ?: "",
                    it,
                )
                emit(emptyList())
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getEvent(eventId: String): Flow<EventResponse> =
        flow {
            val eventDocument = eventsCollection.document(eventId)
            val eventSnapshotFlow = eventDocument.snapshots()

            emitAll(
                eventSnapshotFlow.flatMapConcat { eventSnapshot ->
                    if (!eventSnapshot.exists()) return@flatMapConcat flowOf(EventResponse())
                    val eventResponse =
                        eventSnapshot.toObject<FirebaseEventResponse>()
                            ?.copy(id = eventId)
                            ?.toEventResponse() ?: EventResponse()

                    val groupsCollection = eventDocument.collection(GROUPS_COLLECTION)
                    val groupsSnapshotFlow =
                        groupsCollection.snapshots {
                            it.toObject<GroupResponse>()
                                ?.copy(id = it.id)
                        }

                    groupsSnapshotFlow.flatMapConcat { groupsSnapshot ->
                        val groupsFlow =
                            getGroupsFlow(
                                groupsSnapshot,
                                groupsCollection,
                            )

                        groupsFlow.combineAll()
                            .mapNotNull { groups ->
                                eventResponse.copy(groups = groups)
                            }
                    }
                },
            )
        }.catch {
            logger.e(
                it.message ?: "",
                it,
            )
            emit(EventResponse())
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getGroupsFlow(
        groupsSnapshot: List<GroupResponse>,
        groupsCollection: CollectionReference,
    ) = groupsSnapshot.map { groupResponse ->
        groupResponse.id?.let { id ->
            val nucleusCollection =
                groupsCollection.document(id)
                    .collection(NUCLEUS_COLLECTION)
            val nucleiSnapshotFlow =
                nucleusCollection.snapshots {
                    it.toObject<NucleusResponse>()
                        ?.copy(id = it.id)
                }

            nucleiSnapshotFlow.flatMapConcat { nucleiSnapshot ->
                val nucleiFlow =
                    getNucleiFlow(
                        nucleiSnapshot,
                        nucleusCollection,
                    )

                nucleiFlow.combineAll()
                    .mapNotNull { nucleus ->
                        groupResponse.copy(nucleus = nucleus)
                    }
            }
        } ?: flowOf(groupResponse)
    }

    private fun getNucleiFlow(
        nucleiSnapshot: List<NucleusResponse>,
        nucleusCollection: CollectionReference,
    ) = nucleiSnapshot.map { nucleusResponse ->
        nucleusResponse.id?.let { id ->
            val membersCollection =
                nucleusCollection
                    .document(id)
                    .collection(MEMBERS_COLLECTION)
            val membersSnapshotFlow =
                membersCollection.snapshots {
                    it.toObject<MemberResponse>()
                        ?.copy(id = it.id)
                }
            membersSnapshotFlow.mapNotNull { members ->
                nucleusResponse.copy(members = members)
            }
        } ?: flowOf(nucleusResponse)
    }

    override fun addEvent(
        eventDto: EventDto,
        onResult: (Result<String, DataError>) -> Unit,
    ) {
        eventsCollection.add(eventDto)
            .addDocumentReferenceListeners(onResult)
    }

    override fun setEvent(
        eventDto: EventDto,
        eventId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        eventsCollection.document(eventId)
            .set(eventDto.toFirebaseEventDto())
            .addVoidListeners(onResult)
    }

    override fun deleteEvent(
        eventId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        eventsCollection.document(eventId)
            .delete()
            .addVoidListeners(onResult)
    }

    override suspend fun migrateEmailEvents(email: String): EmptyResult<DataError> {
        return try {
            val events =
                eventsCollection.get()
                    .await().documents.map {
                        it.id
                    }
            events.forEach {
                eventsCollection.document(it)
                    .update(
                        ORGANIZER_EMAIL_FIELD,
                        email,
                    )
            }
            Result.Success(Unit)
        } catch (e: FirebaseException) {
            Result.Error(e.toError())
        }
    }

    override suspend fun migrateUidEvents(uid: String): EmptyResult<DataError> {
        return try {
            val events =
                eventsCollection.get()
                    .await().documents.mapNotNull {
                        if (it.get(ORGANIZER_ID_FIELD) != uid) {
                            it.id
                        } else {
                            null
                        }
                    }
            events.forEach {
                eventsCollection.document(it)
                    .update(
                        ORGANIZER_ID_FIELD,
                        uid,
                    )
            }
            Result.Success(Unit)
        } catch (e: FirebaseException) {
            Result.Error(e.toError())
        }
    }
}

internal const val EVENTS_COLLECTION = "events"
internal const val GROUPS_COLLECTION = "groups"
internal const val NUCLEUS_COLLECTION = "nuclei"
internal const val MEMBERS_COLLECTION = "members"
internal const val DATE_FIELD = "date"
internal const val ORGANIZER_ID_FIELD = "organizerId"
internal const val ORGANIZER_EMAIL_FIELD = "organizerEmail"
