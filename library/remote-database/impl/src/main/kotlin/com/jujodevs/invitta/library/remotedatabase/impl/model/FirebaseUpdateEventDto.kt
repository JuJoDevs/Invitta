package com.jujodevs.invitta.library.remotedatabase.impl.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class FirebaseUpdateEventDto(
    val organizerId: String,
    val organizerEmail: String?,
    val name: String,
    val date: Timestamp,
    val description: String,
    val urlWeb: String?,
    val locationName: String?,
    val location: GeoPoint?,
)
