package com.jujodevs.invitta.library.remotedatabase.impl

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.remotedatabase.impl.mapper.toRemoteDatabaseError

fun Task<DocumentReference>.addDocumentReferenceListeners(
    onResult: (Result<String, DataError>) -> Unit,
) {
    this
        .addOnFailureListener {
            onResult(Result.Error(it.toRemoteDatabaseError()))
        }
        .addOnSuccessListener {
            onResult(Result.Success(it.id))
        }
}

fun Task<Void>.addVoidListeners(onResult: (EmptyResult<DataError>) -> Unit) {
    this
        .addOnFailureListener {
            onResult(Result.Error(it.toRemoteDatabaseError()))
        }
        .addOnSuccessListener {
            onResult(Result.Success(Unit))
        }
}
