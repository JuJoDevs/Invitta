package com.jujodevs.invitta.di

import androidx.compose.material3.SnackbarHostState
import com.jujodevs.invitta.InvittApp
import com.jujodevs.invitta.core.presentation.ui.scaffold.ScaffoldViewModel
import com.jujodevs.invitta.domain.AnonymousLoginUseCase
import com.jujodevs.invitta.presentation.navigation.Screens
import com.jujodevs.invitta.presentation.ui.MainViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        single<CoroutineScope> {
            (androidApplication() as InvittApp).applicationScope
        }
        single { AnonymousLoginUseCase(get()) }
        single { SnackbarHostState() }
        single { Screens(get()) }
        viewModelOf(::ScaffoldViewModel)
        viewModelOf(::MainViewModel)
    }
