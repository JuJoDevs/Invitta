package com.jujodevs.invitta.library.googleauth.impl.di

import com.jujodevs.invitta.core.coroutines.MAIN_COROUTINE
import com.jujodevs.invitta.library.googleauth.api.GoogleAuth
import com.jujodevs.invitta.library.googleauth.impl.DefaultGoogleAuth
import org.koin.core.qualifier.named
import org.koin.dsl.module

val googleAuthModule =
    module {
        single<GoogleAuth> {
            DefaultGoogleAuth(
                remoteConfigProvider = get(),
                credentialManager = get(),
                activityProvider = get(),
                logger = get(),
                mainDispatcher = get(named(MAIN_COROUTINE)),
            )
        }
    }
