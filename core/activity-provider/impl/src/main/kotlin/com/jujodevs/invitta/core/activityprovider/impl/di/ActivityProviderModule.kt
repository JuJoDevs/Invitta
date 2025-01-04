package com.jujodevs.invitta.core.activityprovider.impl.di

import com.jujodevs.invitta.core.activityprovider.api.ActivityProvider
import com.jujodevs.invitta.core.activityprovider.impl.DefaultActivityProvider
import org.koin.dsl.module

val activityProviderModule =
    module {
        single<ActivityProvider>(createdAtStart = true) { DefaultActivityProvider(get()) }
    }
