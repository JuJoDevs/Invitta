package com.jujodevs.invitta.library.remotedatabase.api

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.GroupDto

interface RemoteGroupDatabase {
    fun addGroup(
        group: GroupDto,
        eventId: String,
        onResult: (Result<String, DataError>) -> Unit,
    )
    fun setGroup(
        group: GroupDto,
        eventId: String,
        groupId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    )
    fun deleteGroup(
        eventId: String,
        groupId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    )
}
