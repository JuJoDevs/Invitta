package com.jujodevs.invitta.library.userRepository.impl.data.mapper

import com.jujodevs.invitta.library.remotedatabase.api.model.response.UserResponse
import com.jujodevs.invitta.library.userRepository.api.User

internal fun UserResponse.toDomain(): User {
    return User(
        id = id.orEmpty(),
        name = name.orEmpty(),
        email = email.orEmpty(),
        phone = phone.orEmpty(),
        authorizedEventMembers = authorizedEventMembers,
    )
}
