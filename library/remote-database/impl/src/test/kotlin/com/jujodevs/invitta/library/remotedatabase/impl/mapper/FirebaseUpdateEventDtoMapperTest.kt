package com.jujodevs.invitta.library.remotedatabase.impl.mapper

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.UpdateEventDto
import com.jujodevs.invitta.library.remotedatabase.impl.model.FirebaseUpdateEventDto
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class FirebaseUpdateEventDtoMapperTest {
    @Test
    fun `GIVEN FirebaseEventResponse WHEN toEventResponse THEN returns mapped EventResponse`() {
        val expectedOrganizerId = "organizer123"
        val expectedOrganizerEmail = "organizer@example.com"
        val expectedName = "Sample Event"
        val dateSeconds = 1672531200L
        val expectedFirebaseTimestamp = Timestamp(dateSeconds, 0)
        val expectedDescription = "This is a test event"
        val expectedUrlWeb = "https://example.com"
        val expectedLocationName = "San Francisco"
        val latitude = 37.7749
        val longitude = -122.4194
        val expectedFirebaseGeoPoint = GeoPoint(latitude, longitude)
        val firebaseEventResponse =
            UpdateEventDto(
                organizerId = expectedOrganizerId,
                organizerEmail = expectedOrganizerEmail,
                name = expectedName,
                dateSeconds = dateSeconds,
                description = expectedDescription,
                urlWeb = expectedUrlWeb,
                locationName = expectedLocationName,
                latitude = latitude,
                longitude = longitude,
            )

        val eventResponse = firebaseEventResponse.toFirebaseEventDto()

        eventResponse shouldBeEqualTo
            FirebaseUpdateEventDto(
                organizerId = expectedOrganizerId,
                organizerEmail = expectedOrganizerEmail,
                name = expectedName,
                date = expectedFirebaseTimestamp,
                description = expectedDescription,
                urlWeb = expectedUrlWeb,
                locationName = expectedLocationName,
                location = expectedFirebaseGeoPoint,
            )
    }
}
