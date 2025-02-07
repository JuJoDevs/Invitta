package com.jujodevs.invitta.feature.home.impl.data.mapper

import com.jujodevs.invitta.feature.home.impl.common.DEFAULT_EVENT_DATE_SECONDS
import com.jujodevs.invitta.feature.home.impl.common.DEFAULT_EVENT_DESCRIPTION
import com.jujodevs.invitta.feature.home.impl.common.DEFAULT_EVENT_ID
import com.jujodevs.invitta.feature.home.impl.common.DEFAULT_EVENT_LATITUDE
import com.jujodevs.invitta.feature.home.impl.common.DEFAULT_EVENT_LOCATION_NAME
import com.jujodevs.invitta.feature.home.impl.common.DEFAULT_EVENT_LONGITUDE
import com.jujodevs.invitta.feature.home.impl.common.DEFAULT_EVENT_NAME
import com.jujodevs.invitta.feature.home.impl.common.DEFAULT_EVENT_ORGANIZER_EMAIL
import com.jujodevs.invitta.feature.home.impl.common.DEFAULT_EVENT_ORGANIZER_ID
import com.jujodevs.invitta.feature.home.impl.common.DEFAULT_EVENT_URL_WEB
import com.jujodevs.invitta.feature.home.impl.common.defaultEventResponse
import com.jujodevs.invitta.library.remotedatabase.api.model.response.EventResponse
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class EventResponseMapperTest {
    @Test
    fun `GIVEN EventResponse with valid data WHEN toDomain THEN returns correctly mapped Event`() {
        val eventResponse = defaultEventResponse

        val result = eventResponse.toDomain()

        result.id shouldBeEqualTo DEFAULT_EVENT_ID
        result.organizerId shouldBeEqualTo DEFAULT_EVENT_ORGANIZER_ID
        result.organizerEmail shouldBeEqualTo DEFAULT_EVENT_ORGANIZER_EMAIL
        result.name shouldBeEqualTo DEFAULT_EVENT_NAME
        result.dateSeconds shouldBeEqualTo DEFAULT_EVENT_DATE_SECONDS
        result.description shouldBeEqualTo DEFAULT_EVENT_DESCRIPTION
        result.urlWeb shouldBeEqualTo DEFAULT_EVENT_URL_WEB
        result.locationName shouldBeEqualTo DEFAULT_EVENT_LOCATION_NAME
        result.latitude shouldBeEqualTo DEFAULT_EVENT_LATITUDE
        result.longitude shouldBeEqualTo DEFAULT_EVENT_LONGITUDE
    }

    @Test
    fun `GIVEN EventResponse with null values WHEN toDomain THEN returns Event with default values`() {
        val responseWithNulls = EventResponse()
        val expectedEmptyResult = ""
        val expectedDateSeconds = 0L
        val expectedLatitude = 0.0
        val expectedLongitude = 0.0

        val result = responseWithNulls.toDomain()

        result.id shouldBeEqualTo expectedEmptyResult
        result.organizerId shouldBeEqualTo expectedEmptyResult
        result.organizerEmail shouldBeEqualTo expectedEmptyResult
        result.name shouldBeEqualTo expectedEmptyResult
        result.dateSeconds shouldBeEqualTo expectedDateSeconds
        result.description shouldBeEqualTo expectedEmptyResult
        result.urlWeb shouldBeEqualTo expectedEmptyResult
        result.locationName shouldBeEqualTo expectedEmptyResult
        result.latitude shouldBeEqualTo expectedLatitude
        result.longitude shouldBeEqualTo expectedLongitude
    }
}
