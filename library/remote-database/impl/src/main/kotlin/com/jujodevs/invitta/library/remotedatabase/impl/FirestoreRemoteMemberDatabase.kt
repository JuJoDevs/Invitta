package com.jujodevs.invitta.library.remotedatabase.impl

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.library.remotedatabase.api.RemoteMemberDatabase
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.MemberDto

class FirestoreRemoteMemberDatabase(
    db: FirebaseFirestore,
) : RemoteMemberDatabase {
    private val eventsCollection = db.collection(EVENTS_COLLECTION)

    override fun setMember(
        member: MemberDto,
        eventId: String,
        groupId: String,
        nucleusId: String,
        memberId: String?,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        val eventDocument = eventsCollection.document(eventId)
        val groupDocument = eventDocument.collection(GROUPS_COLLECTION).document(groupId)
        val nucleusDocument = groupDocument.collection(NUCLEUS_COLLECTION).document(nucleusId)
        val memberCollection = nucleusDocument.collection(MEMBERS_COLLECTION)
        if (memberId == null) {
            val mMemberId = memberCollection.document().id
            completeSetMember(member, memberCollection, mMemberId, onResult)
            eventDocument.update(AUTHORIZED_MEMBER_IDS_FIELD, FieldValue.arrayUnion(mMemberId))
            groupDocument.update(AUTHORIZED_MEMBER_IDS_FIELD, FieldValue.arrayUnion(mMemberId))
            nucleusDocument.update(AUTHORIZED_MEMBER_IDS_FIELD, FieldValue.arrayUnion(mMemberId))
        } else {
            completeSetMember(member, memberCollection, memberId, onResult)
        }
    }

    private fun completeSetMember(
        member: MemberDto,
        memberCollection: CollectionReference,
        memberId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        memberCollection
            .document(memberId)
            .set(member)
            .addListeners(onResult)
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
            .addListeners(onResult)

        eventDocument.update(AUTHORIZED_MEMBER_IDS_FIELD, FieldValue.arrayRemove(memberId))
        groupDocument.update(AUTHORIZED_MEMBER_IDS_FIELD, FieldValue.arrayRemove(memberId))
        nucleusDocument.update(AUTHORIZED_MEMBER_IDS_FIELD, FieldValue.arrayRemove(memberId))
    }
}

internal const val AUTHORIZED_MEMBER_IDS_FIELD = "authorizedMemberIds"
