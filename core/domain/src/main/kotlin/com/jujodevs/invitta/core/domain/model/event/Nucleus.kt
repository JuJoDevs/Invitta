package com.jujodevs.invitta.core.domain.model.event

data class Nucleus(
    val id: String,
    val name: String,
    val members: List<Member>,
)
