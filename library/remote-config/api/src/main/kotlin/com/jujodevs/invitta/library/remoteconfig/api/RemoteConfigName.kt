package com.jujodevs.invitta.library.remoteconfig.api

enum class RemoteConfigName(
    val type: RemoteConfigType,
) {
    SHOW_SHARE_APP(RemoteConfigType.BOOLEAN),
    GOOGLE_WEB_CLIENT_ID(RemoteConfigType.STRING),
    SUPABASE_URL(RemoteConfigType.STRING),
    SUPABASE_ANON_KEY(RemoteConfigType.STRING),
}
