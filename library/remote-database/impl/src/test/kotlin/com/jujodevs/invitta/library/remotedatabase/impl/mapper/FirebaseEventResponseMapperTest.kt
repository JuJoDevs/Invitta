package com.jujodevs.invitta.library.remotedatabase.impl.mapper

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.jujodevs.invitta.library.remotedatabase.api.model.response.EventResponse
import com.jujodevs.invitta.library.remotedatabase.api.model.response.GroupResponse
import com.jujodevs.invitta.library.remotedatabase.impl.model.FirebaseEventResponse
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class FirebaseEventResponseMapperTest {
    @Test
    fun `GIVEN FirebaseEventResponse WHEN toEventResponse THEN returns mapped EventResponse`() {
        val expectedId = "event123"
        val expectedOrganizerId = "organizer123"
        val expectedOrganizerEmail = "organizer@example.com"
        val expectedName = "Sample Event"
        val expectedDateSeconds = 1672531200L
        val expectedDescription = "This is a test event"
        val expectedUrlWeb = "https://example.com"
        val expectedLocationName = "San Francisco"
        val expectedLatitude = 37.7749
        val expectedLongitude = -122.4194
        val expectedAuthorizedMembersId = listOf("member1", "member2")
        val expectedGroups =
            listOf(
                GroupResponse(
                    id = "group1",
                    name = "Test Group",
                    nucleus = emptyList(),
                ),
            )
        val firebaseTimestamp = Timestamp(expectedDateSeconds, 0)
        val firebaseGeoPoint = GeoPoint(expectedLatitude, expectedLongitude)
        val firebaseEventResponse =
            FirebaseEventResponse(
                id = expectedId,
                organizerId = expectedOrganizerId,
                organizerEmail = expectedOrganizerEmail,
                name = expectedName,
                date = firebaseTimestamp,
                description = expectedDescription,
                urlWeb = expectedUrlWeb,
                locationName = expectedLocationName,
                location = firebaseGeoPoint,
                authorizedMembersId = expectedAuthorizedMembersId,
                groups = expectedGroups,
            )

        val eventResponse = firebaseEventResponse.toEventResponse()

        eventResponse shouldBeEqualTo
            EventResponse(
                id = expectedId,
                organizerId = expectedOrganizerId,
                organizerEmail = expectedOrganizerEmail,
                name = expectedName,
                dateSeconds = expectedDateSeconds,
                description = expectedDescription,
                urlWeb = expectedUrlWeb,
                locationName = expectedLocationName,
                latitude = expectedLatitude,
                longitude = expectedLongitude,
                groups = expectedGroups,
            )
    }
}
