package com.jujodevs.invitta.library.logger.impl

import com.jujodevs.invitta.library.logger.api.Logger
import org.koin.dsl.module

val loggerModule =
    module {
        single<Logger>(createdAtStart = true) { DefaultLogger() }
    }
