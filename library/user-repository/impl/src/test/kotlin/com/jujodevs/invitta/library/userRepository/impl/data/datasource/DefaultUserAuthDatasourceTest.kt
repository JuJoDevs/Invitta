package com.jujodevs.invitta.library.userRepository.impl.data.datasource

import com.jujodevs.invitta.core.testing.verifyOnce
import com.jujodevs.invitta.library.authservice.api.AuthService
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultUserAuthDatasourceTest {
    private lateinit var datasource: DefaultUserAuthDatasource
    private val authService: AuthService = mockk()

    @BeforeEach
    fun setup() {
        datasource = DefaultUserAuthDatasource(authService)
    }

    @Test
    fun `GIVEN AuthService WHEN getUid THEN returns user ID`() {
        val expectedUid = "user_123"
        every { authService.getCurrentUserId() } returns expectedUid

        val uid = datasource.getUid()

        uid shouldBeEqualTo expectedUid
        verifyOnce { authService.getCurrentUserId() }
    }
}
