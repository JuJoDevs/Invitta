package com.jujodevs.invitta.library.remotedatabase.api.model.dto

data class GroupDto(
    val name: String,
    val authorizedMembersId: List<String> = emptyList(),
)
