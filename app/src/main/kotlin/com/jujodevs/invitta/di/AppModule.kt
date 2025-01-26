package com.jujodevs.invitta.di

import com.jujodevs.invitta.InvittApp
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
        viewModel { MainViewModel(get()) }
    }
