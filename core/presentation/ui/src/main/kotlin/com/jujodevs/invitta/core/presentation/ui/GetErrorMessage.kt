package com.jujodevs.invitta.core.presentation.ui

import android.content.Context
import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Error
import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.presentation.stringresources.R

fun Context.getErrorMessage(error: Error): String {
    return when (error) {
        is LoginError ->
            error.asUiText()
                .asString(this)

        is DataError ->
            error.asUiText()
                .asString(this)

        else -> getString(R.string.unknown_error)
    }
}
