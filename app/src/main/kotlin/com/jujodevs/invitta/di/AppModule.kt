package com.jujodevs.invitta.di

import com.jujodevs.invitta.InvittApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule =
    module {
        single<CoroutineScope> {
            (androidApplication() as InvittApp).applicationScope
        }
    }
