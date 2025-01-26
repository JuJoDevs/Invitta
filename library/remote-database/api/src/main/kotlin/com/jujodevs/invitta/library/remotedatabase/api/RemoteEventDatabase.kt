package com.jujodevs.invitta.library.remotedatabase.api

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.EventDto
import com.jujodevs.invitta.library.remotedatabase.api.model.response.EventResponse
import kotlinx.coroutines.flow.Flow

interface RemoteEventDatabase {
    fun getEvents(): Flow<List<EventResponse>>
    fun getEvent(eventId: String): Flow<EventResponse>
    fun addEvent(
        eventDto: EventDto,
        onResult: (Result<String, DataError>) -> Unit,
    )

    fun setEvent(
        eventDto: EventDto,
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
