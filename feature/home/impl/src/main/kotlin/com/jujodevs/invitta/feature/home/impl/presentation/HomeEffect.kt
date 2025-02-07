package com.jujodevs.invitta.feature.home.impl.presentation

import com.jujodevs.invitta.core.domain.Error

internal sealed interface HomeEffect {
    data class ShowError(
        val error: Error,
    ) : HomeEffect
}
