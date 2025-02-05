package com.jujodevs.invitta.library.userRepository.impl.data

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.userRepository.api.User
import com.jujodevs.invitta.library.userRepository.api.UserRepository
import kotlinx.coroutines.flow.Flow

internal class DefaultUserRepository(
    private val userAuthDatasource: UserAuthDatasource,
    private val userRemoteDatasource: UserRemoteDatasource,
) : UserRepository {
    override fun getUser(): Flow<Result<User, DataError>> {
        return userRemoteDatasource.getUser(userAuthDatasource.getUid())
    }
}
