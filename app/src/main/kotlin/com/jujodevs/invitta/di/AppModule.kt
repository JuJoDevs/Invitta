package com.jujodevs.invitta.di

import androidx.compose.material3.SnackbarHostState
import com.jujodevs.invitta.InvittApp
import com.jujodevs.invitta.core.presentation.ui.scaffold.ScaffoldViewModel
import com.jujodevs.invitta.domain.AnonymousLoginUseCase
import com.jujodevs.invitta.ui.MainViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        single<CoroutineScope> {
            (androidApplication() as InvittApp).applicationScope
        }
        single { AnonymousLoginUseCase(get()) }
        single { SnackbarHostState() }
        viewModel { MainViewModel(get()) }
        viewModel { ScaffoldViewModel(get()) }
    }
