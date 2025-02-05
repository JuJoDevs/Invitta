package com.jujodevs.invitta.library.userRepository.api

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val authorizedEventMembers: Map<String, String>,
)
