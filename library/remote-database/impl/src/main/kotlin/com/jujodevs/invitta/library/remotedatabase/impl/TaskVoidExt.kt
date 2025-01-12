package com.jujodevs.invitta.library.remotedatabase.impl

import com.google.android.gms.tasks.Task
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.remotedatabase.impl.mapper.toRemoteDatabaseError

fun Task<Void>.addListeners(onResult: (EmptyResult<DataError>) -> Unit) {
    this
        .addOnFailureListener {
            onResult(Result.Error(it.toRemoteDatabaseError()))
        }
        .addOnSuccessListener {
            onResult(Result.Success(Unit))
        }
}
