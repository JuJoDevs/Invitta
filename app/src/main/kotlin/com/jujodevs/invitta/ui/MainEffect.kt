package com.jujodevs.invitta.ui

import com.jujodevs.invitta.core.domain.Error

internal interface MainEffect {
    data class ShowError(
        val error: Error,
    ) : MainEffect
}
