package com.jujodevs.invitta.library.remotedatabase.api

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.EmptyResult
import com.jujodevs.invitta.core.domain.Result
import com.jujodevs.invitta.library.remotedatabase.api.model.dto.NucleusDto

interface RemoteNucleusDatabase {
    fun addNucleus(
        nucleus: NucleusDto,
        eventId: String,
        groupId: String,
        onResult: (Result<String, DataError>) -> Unit,
    )
    fun setNucleus(
        nucleus: NucleusDto,
        eventId: String,
        groupId: String,
        nucleusId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    )
    fun deleteNucleus(
        eventId: String,
        groupId: String,
        nucleusId: String,
        onResult: (EmptyResult<DataError>) -> Unit,
    )
}
