package com.jujodevs.invitta.library.remotedatabase.api.model.dto

data class UserDto(
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val authorizedEventMembers: Map<String, String> = emptyMap(),
)
