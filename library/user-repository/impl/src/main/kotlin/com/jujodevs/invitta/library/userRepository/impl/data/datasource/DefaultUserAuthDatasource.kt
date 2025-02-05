package com.jujodevs.invitta.library.userRepository.impl.data.datasource

import com.jujodevs.invitta.library.authservice.api.AuthService
import com.jujodevs.invitta.library.userRepository.impl.data.UserAuthDatasource

internal class DefaultUserAuthDatasource(
    private val authService: AuthService,
) : UserAuthDatasource {
    override fun getUid(): String {
        return authService.getCurrentUserId()
    }
}
