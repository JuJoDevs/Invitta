package com.jujodevs.invitta.library.remotedatabase.api.model.dto

data class UpdateEventDto(
    val organizerEmail: String? = null,
    val name: String,
    val dateSeconds: Long,
    val description: String,
    val urlWeb: String? = null,
    val locationName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
