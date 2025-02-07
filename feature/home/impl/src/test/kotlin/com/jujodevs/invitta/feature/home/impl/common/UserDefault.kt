package com.jujodevs.invitta.feature.home.impl.common

import com.jujodevs.invitta.library.userRepository.api.User

const val DEFAULT_USER_ID = "123"
const val DEFAULT_USER_NAME = "John Doe"
const val DEFAULT_USER_EMAIL = "john.doe@example.com"
const val DEFAULT_USER_PHONE = "1234567890"
val defaultAuthorizedEventMembers = mapOf("event1" to "member1")

val defaultUser =
    User(
        id = DEFAULT_USER_ID,
        name = DEFAULT_USER_NAME,
        email = DEFAULT_USER_EMAIL,
        phone = DEFAULT_USER_PHONE,
        authorizedEventMembers = defaultAuthorizedEventMembers,
    )
