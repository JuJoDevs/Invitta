package com.jujodevs.invitta.library.remotedatabase.impl

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.remotedatabase.api.RemoteMemberDatabase
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.MemberDto

class FirestoreRemoteMemberDatabase(
    db: FirebaseFirestore,
) : RemoteMemberDatabase {
    private val eventsCollection = db.collection(EVENTS_COLLECTION)
    override fun addMember(
        member: MemberDto,
        eventId: String,
        groupId: String,
        nucleusId: String,
        onResult: (Result<String, DataError>) -> Unit,
    ) {
        val eventDocument = eventsCollection.document(eventId)
        val groupDocument = eventDocument.collection(GROUPS_COLLECTION).document(groupId)
        val nucleusDocument = groupDocument.collection(NUCLEUS_COLLECTION).document(nucleusId)
        val memberCollection = nucleusDocument.collection(MEMBERS_COLLECTION)
        memberCollection.add(member)
            .addDocumentReferenceListeners { result ->
                when (result) {
                    is Result.Error -> onResult(result)
                    is Result.Success -> {
                        eventDocument.update(
                            AUTHORIZED_MEMBER_IDS_FIELD,
                            FieldValue.arrayUnion(result.data),
                        )
                        groupDocument.update(
                            AUTHORIZED_MEMBER_IDS_FIELD,
                            FieldValue.arrayUnion(result.data),
                        )
                        nucleusDocument.update(
                            AUTHORIZED_MEMBER_IDS_FIELD,
                            FieldValue.arrayUnion(result.data),
                        )
                        onResult(Result.Success(result.data))
                    }
                }
            }
    }

    override fun setMember(
        member: MemberDto,
        eventId: String,
        groupId: String,
        nucleusId: String,
        memberId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        eventsCollection.document(eventId)
            .collection(GROUPS_COLLECTION).document(groupId)
            .collection(NUCLEUS_COLLECTION).document(nucleusId)
            .collection(MEMBERS_COLLECTION)
            .document(memberId)
            .set(member)
            .addVoidListeners(onResult)
    }

    override fun deleteMember(
        eventId: String,
        groupId: String,
        nucleusId: String,
        memberId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        val eventDocument = eventsCollection.document(eventId)
        val groupDocument = eventDocument.collection(GROUPS_COLLECTION).document(groupId)
        val nucleusDocument = groupDocument.collection(NUCLEUS_COLLECTION).document(nucleusId)
        val memberDocument = nucleusDocument.collection(MEMBERS_COLLECTION).document(memberId)
        memberDocument.delete()
            .addVoidListeners(onResult)

        eventDocument.update(AUTHORIZED_MEMBER_IDS_FIELD, FieldValue.arrayRemove(memberId))
        groupDocument.update(AUTHORIZED_MEMBER_IDS_FIELD, FieldValue.arrayRemove(memberId))
        nucleusDocument.update(AUTHORIZED_MEMBER_IDS_FIELD, FieldValue.arrayRemove(memberId))
    }
}

internal const val AUTHORIZED_MEMBER_IDS_FIELD = "authorizedMemberIds"
