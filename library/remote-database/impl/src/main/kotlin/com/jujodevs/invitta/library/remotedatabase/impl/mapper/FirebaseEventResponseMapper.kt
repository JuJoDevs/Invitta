package com.jujodevs.invitta.library.remotedatabase.impl.mapper

import com.jujodevs.invitta.library.remotedatabase.api.model.response.EventResponse
import com.jujodevs.invitta.library.remotedatabase.impl.model.FirebaseEventResponse

fun FirebaseEventResponse.toEventResponse(): EventResponse {
    return EventResponse(
        id = id,
        organizerId = organizerId,
        organizerEmail = organizerEmail,
        name = name,
        dateSeconds = date?.seconds,
        description = description,
        urlWeb = urlWeb,
        locationName = locationName,
        latitude = location?.latitude,
        longitude = location?.longitude,
        groups = groups,
    )
}
