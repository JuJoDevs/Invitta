package com.jujodevs.invitta.library.remotedatabase.api.model.response

data class GroupResponse(
    val id: String? = null,
    val name: String? = null,
    val nucleus: List<NucleusResponse> = emptyList(),
)
