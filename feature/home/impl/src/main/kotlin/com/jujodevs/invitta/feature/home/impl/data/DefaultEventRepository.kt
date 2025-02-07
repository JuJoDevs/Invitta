package com.jujodevs.invitta.feature.home.impl.data

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.domain.model.event.Event
import com.jujodevs.invitta.feature.home.impl.domain.EventRepository
import kotlinx.coroutines.flow.Flow

internal class DefaultEventRepository(
    private val remoteDatasource: EventRemoteDatasource,
) : EventRepository {
    override fun getEvents(
        uid: String,
        authorizedMemberIds: List<String>,
    ): Flow<Result<List<Event>, DataError>> {
        return remoteDatasource.getEvents(uid, authorizedMemberIds)
    }
}
