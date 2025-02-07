package com.jujodevs.invitta.core.domain.model.event

data class Member(
    val id: String,
    val uid: String,
    val name: String,
    val email: String,
    val phone: Int,
    val type: Int,
    val status: Int,
)
