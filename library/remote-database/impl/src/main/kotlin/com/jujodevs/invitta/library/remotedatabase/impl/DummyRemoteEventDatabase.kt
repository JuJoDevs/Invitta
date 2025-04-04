package com.jujodevs.invitta.library.remotedatabase.impl

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.remotedatabase.api.RemoteEventDatabase
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.EventDto
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.UpdateEventDto
import com.jujodevs.invitta.library.remotedatabase.api.model.response.EventResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DummyRemoteEventDatabase : RemoteEventDatabase {
    override fun getEvents(
        uid: String,
        authorizedMemberIds: List<String>,
    ): Flow<Result<List<EventResponse>, DataError>> {
        return flowOf(Result.Success(emptyList()))
    }

    override fun getEvent(
        uidOrMember: String,
        eventId: String,
    ): Flow<Result<EventResponse, DataError>> {
        return flowOf(Result.Success(EventResponse()))
    }

    override fun addEvent(
        eventDto: EventDto,
        onResult: (Result<String, DataError>) -> Unit,
    ) {
        Unit
    }

    override fun setEvent(
        eventDto: UpdateEventDto,
        eventId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        Unit
    }

    override fun deleteEvent(
        eventId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    ) {
        Unit
    }

    override suspend fun migrateEmailEvents(email: String): EmptyResult<DataError> {
        return Result.Success(Unit)
    }

    override suspend fun migrateUidEvents(uid: String): EmptyResult<DataError> {
        return Result.Success(Unit)
    }
}
