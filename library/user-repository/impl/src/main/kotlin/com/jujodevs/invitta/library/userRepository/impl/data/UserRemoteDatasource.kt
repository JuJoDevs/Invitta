package com.jujodevs.invitta.library.userRepository.impl.data

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.userRepository.api.User
import kotlinx.coroutines.flow.Flow

internal interface UserRemoteDatasource {
    fun getUser(uid: String): Flow<Result<User, DataError>>
}
