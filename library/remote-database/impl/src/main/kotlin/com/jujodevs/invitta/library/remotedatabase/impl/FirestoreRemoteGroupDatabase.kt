package com.jujodevs.invitta.library.remotedatabase.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.remotedatabase.api.RemoteGroupDatabase
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.GroupDto

class FirestoreRemoteGroupDatabase(
    db: FirebaseFirestore,
) : RemoteGroupDatabase {
    private val eventsCollection = db.collection(EVENTS_COLLECTION)
    override fun addGroup(
        group: GroupDto,
        eventId: String,
        onResult: (Result<String, DataError>) -> Unit,
    ) {
        eventsCollection.document(eventId)
            .collection(GROUPS_COLLECTION)
            .add(group)
            .addDocumentReferenceListeners(onResult)
    }

    override fun setGroup(
        group: GroupDto,
        eventId: String,
        groupId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        eventsCollection.document(eventId)
            .collection(GROUPS_COLLECTION)
            .document(groupId)
            .set(group)
            .addVoidListeners(onResult)
    }

    override fun deleteGroup(
        eventId: String,
        groupId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        eventsCollection.document(eventId)
            .collection(GROUPS_COLLECTION)
            .document(groupId)
            .delete()
            .addVoidListeners(onResult)
    }
}
