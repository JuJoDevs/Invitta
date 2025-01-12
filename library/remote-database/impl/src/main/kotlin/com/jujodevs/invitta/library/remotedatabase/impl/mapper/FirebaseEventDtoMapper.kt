package com.jujodevs.invitta.library.remotedatabase.impl.mapper

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.EventDto
import com.jujodevs.invitta.library.remotedatabase.impl.model.FirebaseEventDto

fun EventDto.toFirebaseEventDto(): FirebaseEventDto {
    return FirebaseEventDto(
        organizerId = organizerId,
        organizerEmail = organizerEmail,
        name = name,
        date = Timestamp(dateSeconds, 0),
        description = description,
        urlWeb = urlWeb,
        locationName = locationName,
        location =
            latitude?.let { mLatitude ->
                longitude?.let { mLongitude ->
                    GeoPoint(mLatitude, mLongitude)
                }
            },
        authorizedMembersId = authorizedMembersId,
    )
}
