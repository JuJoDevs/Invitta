package com.jujodevs.invitta.presentation.navigation

import kotlinx.serialization.Serializable

internal sealed class Route {
    @Serializable
    data object Home : Route()
}
