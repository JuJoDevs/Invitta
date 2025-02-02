package com.jujodevs.invitta.library.remotedatabase.api.model.response

data class EventResponse(
    val id: String? = null,
    val organizerId: String? = null,
    val organizerEmail: String? = null,
    val name: String? = null,
    val dateSeconds: Long? = null,
    val description: String? = null,
    val urlWeb: String? = null,
    val locationName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val groups: List<GroupResponse> = emptyList(),
)
