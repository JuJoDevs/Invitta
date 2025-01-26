package com.jujodevs.invitta.feature.home.impl.di

import com.jujodevs.invitta.feature.home.impl.presentation.HomeScreen
import org.koin.dsl.module

val homeModule =
    module {
        single { HomeScreen() }
    }
