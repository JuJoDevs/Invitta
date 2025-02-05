package com.jujodevs.invitta.library.userRepository.api

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<Result<User, DataError>>
}
