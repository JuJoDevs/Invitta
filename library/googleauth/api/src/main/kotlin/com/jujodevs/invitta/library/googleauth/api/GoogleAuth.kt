package com.jujodevs.invitta.library.googleauth.api

import com.jujodevs.invitta.core.domain.Error
import com.jujodevs.invitta.core.domain.Result

interface GoogleAuth {
    suspend fun login(): Result<String, Error>
}
