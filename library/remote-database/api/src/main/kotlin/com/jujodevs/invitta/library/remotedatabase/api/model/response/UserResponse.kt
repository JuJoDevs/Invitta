package com.jujodevs.invitta.library.remotedatabase.api.model.response

data class UserResponse(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val authorizedEventMembers: Map<String, String> = emptyMap(),
)
