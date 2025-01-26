package com.jujodevs.invitta.library.remotedatabase.api

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.MemberDto

interface RemoteMemberDatabase {
    fun addMember(
        member: MemberDto,
        eventId: String,
        groupId: String,
        nucleusId: String,
        onResult: (Result<String, DataError>) -> Unit,
    )
    fun setMember(
        member: MemberDto,
        eventId: String,
        groupId: String,
        nucleusId: String,
        memberId: String,
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
