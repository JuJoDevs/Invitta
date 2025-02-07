package com.jujodevs.invitta.feature.home.impl.common

import com.jujodevs.invitta.core.domain.model.event.Event
import com.jujodevs.invitta.library.remotedatabase.api.model.response.EventResponse

const val DEFAULT_EVENT_ID = "event_123"
const val DEFAULT_EVENT_ORGANIZER_ID = "organizer_456"
const val DEFAULT_EVENT_ORGANIZER_EMAIL = "organizer@example.com"
const val DEFAULT_EVENT_NAME = "Tech Conference"
const val DEFAULT_EVENT_DATE_SECONDS = 1678901234L
const val DEFAULT_EVENT_DESCRIPTION = "A conference about technology"
const val DEFAULT_EVENT_URL_WEB = "https://techconf.com"
const val DEFAULT_EVENT_LOCATION_NAME = "Convention Center"
const val DEFAULT_EVENT_LATITUDE = 40.7128
const val DEFAULT_EVENT_LONGITUDE = -74.0060

val defaultEvent =
    Event(
        id = DEFAULT_EVENT_ID,
        organizerId = DEFAULT_EVENT_ORGANIZER_ID,
        organizerEmail = DEFAULT_EVENT_ORGANIZER_EMAIL,
        name = DEFAULT_EVENT_NAME,
        dateSeconds = DEFAULT_EVENT_DATE_SECONDS,
        description = DEFAULT_EVENT_DESCRIPTION,
        urlWeb = DEFAULT_EVENT_URL_WEB,
        locationName = DEFAULT_EVENT_LOCATION_NAME,
        latitude = DEFAULT_EVENT_LATITUDE,
        longitude = DEFAULT_EVENT_LONGITUDE,
        groups = emptyList(),
    )

val defaultEventResponse =
    EventResponse(
        id = DEFAULT_EVENT_ID,
        organizerId = DEFAULT_EVENT_ORGANIZER_ID,
        organizerEmail = DEFAULT_EVENT_ORGANIZER_EMAIL,
        name = DEFAULT_EVENT_NAME,
        dateSeconds = DEFAULT_EVENT_DATE_SECONDS,
        description = DEFAULT_EVENT_DESCRIPTION,
        urlWeb = DEFAULT_EVENT_URL_WEB,
        locationName = DEFAULT_EVENT_LOCATION_NAME,
        latitude = DEFAULT_EVENT_LATITUDE,
        longitude = DEFAULT_EVENT_LONGITUDE,
    )
