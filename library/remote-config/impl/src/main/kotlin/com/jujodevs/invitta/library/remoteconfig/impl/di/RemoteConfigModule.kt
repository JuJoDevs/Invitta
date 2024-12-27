package com.jujodevs.invitta.library.remoteconfig.impl.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.jujodevs.invitta.library.remoteconfig.api.RemoteConfigProvider
import com.jujodevs.invitta.library.remoteconfig.impl.FirebaseRemoteConfigProvider
import org.koin.dsl.module

val remoteConfigModule =
    module {
        single<FirebaseRemoteConfig> { Firebase.remoteConfig }
        single<RemoteConfigProvider> { FirebaseRemoteConfigProvider(get()) }
    }
