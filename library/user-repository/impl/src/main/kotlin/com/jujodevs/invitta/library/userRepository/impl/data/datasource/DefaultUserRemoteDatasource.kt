package com.jujodevs.invitta.library.userRepository.impl.data.datasource

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.core.domain.map
import com.jujodevs.invitta.library.remotedatabase.api.RemoteUserDatabase
import com.jujodevs.invitta.library.userRepository.api.User
import com.jujodevs.invitta.library.userRepository.impl.data.UserRemoteDatasource
import com.jujodevs.invitta.library.userRepository.impl.data.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultUserRemoteDatasource(
    private val remoteUserDatabase: RemoteUserDatabase,
) : UserRemoteDatasource {
    override fun getUser(uid: String): Flow<Result<User, DataError>> {
        return remoteUserDatabase.getUser(uid).map { result ->
            result.map { it.toDomain() }
        }
    }
}
