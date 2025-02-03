package com.jujodevs.invitta.library.remotedatabase.api

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.EventDto
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.UpdateEventDto
import com.jujodevs.invitta.library.remotedatabase.api.model.response.EventResponse
import kotlinx.coroutines.flow.Flow

interface RemoteEventDatabase {
    fun getEvents(
        uid: String,
        authorizedMemberIds: List<String>,
    ): Flow<Result<List<EventResponse>, DataError>>
    fun getEvent(
        uidOrMember: String,
        eventId: String,
    ): Flow<Result<EventResponse, DataError>>
    fun addEvent(
        eventDto: EventDto,
        onResult: (Result<String, DataError>) -> Unit,
    )

    fun setEvent(
        eventDto: UpdateEventDto,
        eventId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    )

    fun deleteEvent(
        eventId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    )

    suspend fun migrateEmailEvents(email: String): EmptyResult<DataError>
    suspend fun migrateUidEvents(uid: String): EmptyResult<DataError>
}
