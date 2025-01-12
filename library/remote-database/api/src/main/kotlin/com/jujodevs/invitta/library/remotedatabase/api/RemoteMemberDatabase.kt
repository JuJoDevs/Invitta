package com.jujodevs.invitta.library.remotedatabase.api

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.MemberDto

interface RemoteMemberDatabase {
    fun setMember(
        member: MemberDto,
        eventId: String,
        groupId: String,
        nucleusId: String,
        memberId: String? = null,
        onResult: (EmptyResult<DataError>) -> Unit,
    )
    fun deleteMember(
        eventId: String,
        groupId: String,
        nucleusId: String,
        memberId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    )
}
