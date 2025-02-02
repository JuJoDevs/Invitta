package com.jujodevs.invitta.library.remotedatabase.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.logger.api.Logger
import com.jujodevs.invitta.library.remotedatabase.api.RemoteUserDatabase
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.UserDto
import com.jujodevs.invitta.library.remotedatabase.api.model.response.UserResponse
import com.jujodevs.invitta.library.remotedatabase.impl.mapper.toRemoteDatabaseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FirestoreRemoteUserDatabase(
    db: FirebaseFirestore,
    private val logger: Logger,
) : RemoteUserDatabase {
    private val usersCollection = db.collection(USERS_COLLECTION)

    override fun getUser(uid: String): Flow<Result<UserResponse, DataError>> {
        if (uid.isEmpty()) return flowOf(Result.Error(DataError.RemoteDatabase.EMPTY_UID))
        return usersCollection.document(uid)
            .snapshots()
            .map { qs ->
                Result.Success(
                    qs.toObject<UserResponse>()?.copy(id = qs.id) ?: UserResponse(),
                ) as Result<UserResponse, DataError>
            }
            .catch {
                logger.e(
                    it.message ?: "",
                    it,
                )
                emit(Result.Error(it.toRemoteDatabaseError()))
            }
    }

    override fun setUser(
        user: UserDto,
        id: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        usersCollection.document(id).set(user)
            .addVoidListeners(onResult)
    }

    override fun deleteUser(
        id: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        usersCollection.document(id).delete()
            .addVoidListeners(onResult)
    }
}

private const val USERS_COLLECTION = "users"
