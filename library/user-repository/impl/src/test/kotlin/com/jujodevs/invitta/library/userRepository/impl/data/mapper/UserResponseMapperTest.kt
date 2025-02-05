package com.jujodevs.invitta.library.userRepository.impl.data.mapper

import com.jujodevs.invitta.library.remotedatabase.api.model.response.UserResponse
import com.jujodevs.invitta.library.userRepository.impl.common.DEFAULT_USER_EMAIL
import com.jujodevs.invitta.library.userRepository.impl.common.DEFAULT_USER_ID
import com.jujodevs.invitta.library.userRepository.impl.common.DEFAULT_USER_NAME
import com.jujodevs.invitta.library.userRepository.impl.common.DEFAULT_USER_PHONE
import com.jujodevs.invitta.library.userRepository.impl.common.defaultAuthorizedEventMembers
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class UserResponseMapperTest {
    @Test
    fun `GIVEN UserResponse WHEN toDomain THEN returns User with same data`() {
        val expectedId = DEFAULT_USER_ID
        val expectedName = DEFAULT_USER_NAME
        val expectedEmail = DEFAULT_USER_EMAIL
        val expectedPhone = DEFAULT_USER_PHONE
        val expectedAuthorizedEventMembers = defaultAuthorizedEventMembers
        val expectedUserResponse =
            UserResponse(
                id = expectedId,
                name = expectedName,
                email = expectedEmail,
                phone = expectedPhone,
                authorizedEventMembers = expectedAuthorizedEventMembers,
            )

        val user = expectedUserResponse.toDomain()

        user.id shouldBeEqualTo expectedId
        user.name shouldBeEqualTo expectedName
        user.email shouldBeEqualTo expectedEmail
        user.phone shouldBeEqualTo expectedPhone
        user.authorizedEventMembers shouldBeEqualTo expectedAuthorizedEventMembers
    }

    @Test
    fun `GIVEN UserResponse with null values WHEN toDomain THEN returns User with empty defaults`() {
        val expectedEmptyResult = ""
        val expectedEmptyMap = emptyMap<String, String>()
        val emptyResponse = UserResponse()

        val user = emptyResponse.toDomain()

        user.id shouldBeEqualTo expectedEmptyResult
        user.name shouldBeEqualTo expectedEmptyResult
        user.email shouldBeEqualTo expectedEmptyResult
        user.phone shouldBeEqualTo expectedEmptyResult
        user.authorizedEventMembers shouldBeEqualTo expectedEmptyMap
    }
}
