package com.jujodevs.invitta.library.remotedatabase.impl

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

fun <T> CollectionReference.snapshots(map: (DocumentSnapshot) -> T?): Flow<List<T>> {
    return this
        .snapshots()
        .mapNotNull { qs ->
            qs.documents.mapNotNull {
                map(it)
            }
        }
}
