package com.jujodevs.invitta.library.remotedatabase.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.remotedatabase.api.RemoteNucleusDatabase
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.NucleusDto

class FirestoreRemoteNucleusDatabase(
    db: FirebaseFirestore,
) : RemoteNucleusDatabase {
    private val eventsCollection = db.collection(EVENTS_COLLECTION)
    override fun addNucleus(
        nucleus: NucleusDto,
        eventId: String,
        groupId: String,
        onResult: (Result<String, DataError>) -> Unit,
    ) {
        eventsCollection.document(eventId)
            .collection(GROUPS_COLLECTION)
            .document(groupId)
            .collection(NUCLEUS_COLLECTION)
            .add(nucleus)
            .addDocumentReferenceListeners(onResult)
    }

    override fun setNucleus(
        nucleus: NucleusDto,
        eventId: String,
        groupId: String,
        nucleusId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        eventsCollection.document(eventId)
            .collection(GROUPS_COLLECTION)
            .document(groupId)
            .collection(NUCLEUS_COLLECTION)
            .document(nucleusId)
            .set(nucleus)
            .addVoidListeners(onResult)
    }

    override fun deleteNucleus(
        eventId: String,
        groupId: String,
        nucleusId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        eventsCollection.document(eventId)
            .collection(GROUPS_COLLECTION)
            .document(groupId)
            .collection(NUCLEUS_COLLECTION)
            .document(nucleusId)
            .delete()
            .addVoidListeners(onResult)
    }
}
