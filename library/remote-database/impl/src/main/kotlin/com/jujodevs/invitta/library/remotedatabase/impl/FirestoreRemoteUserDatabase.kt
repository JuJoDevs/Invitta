package com.jujodevs.invitta.library.remotedatabase.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.library.logger.api.Logger
import com.jujodevs.invitta.library.remotedatabase.api.RemoteUserDatabase
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.UserDto
import com.jujodevs.invitta.library.remotedatabase.api.model.response.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FirestoreRemoteUserDatabase(
    db: FirebaseFirestore,
    private val logger: Logger,
) : RemoteUserDatabase {
    private val usersCollection = db.collection(USERS_COLLECTION)

    override fun getUser(uid: String): Flow<UserResponse> {
        if (uid.isEmpty()) return flowOf(UserResponse())
        return usersCollection.document(uid)
            .snapshots()
            .map { qs ->
                qs.toObject<UserResponse>()?.copy(id = qs.id) ?: UserResponse()
            }
            .catch {
                logger.e(
                    it.message ?: "",
                    it,
                )
                emit(UserResponse())
            }
    }

    override fun setUser(
        user: UserDto,
        id: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        usersCollection.document(id).set(user)
            .addListeners(onResult)
    }

    override fun deleteUser(
        id: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        usersCollection.document(id).delete()
            .addListeners(onResult)
    }
}

private const val USERS_COLLECTION = "users"
