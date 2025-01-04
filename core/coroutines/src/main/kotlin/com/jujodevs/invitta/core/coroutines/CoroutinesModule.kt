package com.jujodevs.invitta.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coroutinesModule =
    module {
        single<CoroutineDispatcher>(named(MAIN_COROUTINE)) { Dispatchers.Main }
        single<CoroutineDispatcher>(named(IO_COROUTINE)) { Dispatchers.IO }
    }

const val MAIN_COROUTINE = "mainCoroutine"
const val IO_COROUTINE = "ioCoroutine"
