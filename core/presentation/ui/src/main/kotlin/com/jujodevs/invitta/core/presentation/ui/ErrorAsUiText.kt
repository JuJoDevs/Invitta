package com.jujodevs.invitta.core.presentation.ui

import com.jujodevs.invitta.core.domain.DataError
import com.jujodevs.invitta.core.domain.Error
import com.jujodevs.invitta.core.domain.LoginError
import com.jujodevs.invitta.core.presentation.stringresources.R

fun Error.asUiText(): UiText {
    return when (this) {
        is LoginError ->
            this.asUiText()

        is DataError ->
            this.asUiText()

        else -> UiText.StringResource(R.string.unknown_error)
    }
}
