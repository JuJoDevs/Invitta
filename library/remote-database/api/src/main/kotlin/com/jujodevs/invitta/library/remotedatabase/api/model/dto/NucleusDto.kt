package com.jujodevs.invitta.library.remotedatabase.api.model.dto

data class NucleusDto(
    val name: String,
    val organizerId: String,
    val authorizedMembersId: List<String> = emptyList(),
)
