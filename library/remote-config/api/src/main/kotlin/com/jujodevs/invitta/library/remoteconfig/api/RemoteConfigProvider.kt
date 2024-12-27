package com.jujodevs.invitta.library.remoteconfig.api

interface RemoteConfigProvider {
    fun getStringParameter(remoteConfigName: RemoteConfigName): String
    fun getBooleanParameter(remoteConfigName: RemoteConfigName): Boolean
}
