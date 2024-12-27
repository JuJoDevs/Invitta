package com.jujodevs.invitta.library.remoteconfig.api

enum class RemoteConfigName(
    val type: RemoteConfigType,
) {
    SHOW_SHARE_APP(RemoteConfigType.BOOLEAN),
}
