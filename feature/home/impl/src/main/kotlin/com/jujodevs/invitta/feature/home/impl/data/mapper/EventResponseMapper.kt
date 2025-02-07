package com.jujodevs.invitta.feature.home.impl.data.mapper

import com.jujodevs.invitta.core.domain.model.event.Event
import com.jujodevs.invitta.library.remotedatabase.api.model.response.EventResponse

internal fun EventResponse.toDomain(): Event {
    return Event(
        id = id.orEmpty(),
        organizerId = organizerId.orEmpty(),
        organizerEmail = organizerEmail.orEmpty(),
        name = name.orEmpty(),
        dateSeconds = dateSeconds ?: 0L,
        description = description.orEmpty(),
        urlWeb = urlWeb.orEmpty(),
        locationName = locationName.orEmpty(),
        latitude = latitude ?: 0.0,
        longitude = longitude ?: 0.0,
        groups = emptyList(),
    )
}
