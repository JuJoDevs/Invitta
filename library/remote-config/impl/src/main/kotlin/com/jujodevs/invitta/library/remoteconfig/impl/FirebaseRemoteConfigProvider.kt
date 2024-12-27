package com.jujodevs.invitta.library.remoteconfig.impl

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.jujodevs.invitta.library.remoteconfig.api.RemoteConfigName
import com.jujodevs.invitta.library.remoteconfig.api.RemoteConfigProvider

class FirebaseRemoteConfigProvider(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
) : RemoteConfigProvider {
    init {
        val remoteConfigSettings =
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = MINIMUM_FETCH_INTERVAL_IN_SECONDS
            }
        firebaseRemoteConfig.setConfigSettingsAsync(remoteConfigSettings)
        firebaseRemoteConfig.fetchAndActivate()
    }

    override fun getStringParameter(remoteConfigName: RemoteConfigName): String {
        return firebaseRemoteConfig.getString(remoteConfigName.name)
    }

    override fun getBooleanParameter(remoteConfigName: RemoteConfigName): Boolean {
        return firebaseRemoteConfig.getBoolean(remoteConfigName.name)
    }
}

const val MINIMUM_FETCH_INTERVAL_IN_SECONDS = 3_600L
