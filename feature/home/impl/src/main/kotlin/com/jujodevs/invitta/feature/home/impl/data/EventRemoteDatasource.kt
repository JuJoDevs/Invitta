package com.jujodevs.invitta.feature.home.impl.data

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.domain.model.event.Event
import kotlinx.coroutines.flow.Flow

internal interface EventRemoteDatasource {
    fun getEvents(
        uid: String,
        authorizedMemberIds: List<String>,
    ): Flow<Result<List<Event>, DataError>>
}
