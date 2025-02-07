package com.jujodevs.invitta.feature.home.impl.data.datasource

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.domain.map
import com.jujodevs.invitta.core.domain.model.event.Event
import com.jujodevs.invitta.feature.home.impl.data.EventRemoteDatasource
import com.jujodevs.invitta.feature.home.impl.data.mapper.toDomain
import com.jujodevs.invitta.library.remotedatabase.api.RemoteEventDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultEventRemoteDatasource(
    private val remoteEventDatabase: RemoteEventDatabase,
) : EventRemoteDatasource {
    override fun getEvents(
        uid: String,
        authorizedMemberIds: List<String>,
    ): Flow<Result<List<Event>, DataError>> {
        return remoteEventDatabase.getEvents(uid, authorizedMemberIds)
            .map { result ->
                result.map { eventResponses ->
                    eventResponses.map { eventResponse ->
                        eventResponse.toDomain()
                    }
                }
            }
    }
}
