package com.jujodevs.invitta.feature.home.impl.data.datasource

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.domain.model.event.Event
import com.jujodevs.invitta.feature.home.impl.data.EventRemoteDatasource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class DefaultEventRemoteDatasource : EventRemoteDatasource {
    override fun getEvents(
        uid: String,
        authorizedMemberIds: List<String>,
    ): Flow<Result<List<Event>, DataError>> {
        return flowOf(Result.Success(emptyList()))
    }
}
