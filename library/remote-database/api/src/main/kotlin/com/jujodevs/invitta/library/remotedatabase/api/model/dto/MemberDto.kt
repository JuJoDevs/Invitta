package com.jujodevs.invitta.library.remotedatabase.api.model.dto

data class MemberDto(
    val name: String,
    val email: String? = null,
    val phone: Int? = null,
    val type: Int = 0,
    val status: Int = 0,
    val sent: Boolean = false,
)
