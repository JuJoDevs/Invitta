package com.jujodevs.invitta.library.remotedatabase.impl.mapper

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.UpdateEventDto
import com.jujodevs.invitta.library.remotedatabase.impl.model.FirebaseUpdateEventDto

fun UpdateEventDto.toFirebaseEventDto(): FirebaseUpdateEventDto {
    return FirebaseUpdateEventDto(
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
    )
}
