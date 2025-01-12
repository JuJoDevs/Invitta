package com.jujodevs.invitta.library.remotedatabase.api.model.response

data class NucleusResponse(
    val id: String? = null,
    val name: String? = null,
    val authorizedMembersId: List<String> = emptyList(),
    val members: List<MemberResponse> = emptyList(),
)
