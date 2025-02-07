package com.jujodevs.invitta.core.domain.model.event

data class Group(
    val id: String,
    val name: String,
    val nucleus: List<Nucleus>,
)
