package com.jujodevs.invitta.core.domain.model.event

data class Event(
    val id: String,
    val organizerId: String,
    val organizerEmail: String,
    val name: String,
    val dateSeconds: Long,
    val description: String,
    val urlWeb: String,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val groups: List<Group>,
    val userIsOrganizer: Boolean = false,
)
