package com.jujodevs.invitta.library.remotedatabase.impl.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.jujodevs.invitta.library.remotedatabase.api.model.response.GroupResponse

data class FirebaseEventResponse(
    val id: String? = null,
    val organizerId: String? = null,
    val organizerEmail: String? = null,
    val name: String? = null,
    val date: Timestamp? = null,
    val description: String? = null,
    val urlWeb: String? = null,
    val locationName: String? = null,
    val location: GeoPoint? = null,
    val authorizedMembersId: List<String> = emptyList(),
    val groups: List<GroupResponse> = emptyList(),
)
