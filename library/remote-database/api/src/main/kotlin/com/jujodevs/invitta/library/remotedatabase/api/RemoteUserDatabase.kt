package com.jujodevs.invitta.library.remotedatabase.api

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.UserDto
import com.jujodevs.invitta.library.remotedatabase.api.model.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface RemoteUserDatabase {
    fun getUser(uid: String): Flow<UserResponse>
    fun setUser(
        user: UserDto,
        id: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    )
    fun deleteUser(
        id: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    )
}
