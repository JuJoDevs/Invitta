package com.jujodevs.invitta.library.logger.impl

import com.jujodevs.invitta.library.logger.api.Logger
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class DefaultLogger : Logger {
    init {
        Napier.base(DebugAntilog())
    }

    override fun d(
        message: String,
        throwable: Throwable?,
        tag: String?,
    ) {
        Napier.d(
            message = message,
            throwable = throwable,
            tag = tag,
        )
    }

    override fun i(
        message: String,
        throwable: Throwable?,
        tag: String?,
    ) {
        Napier.i(
            message = message,
            throwable = throwable,
            tag = tag,
        )
    }

    override fun w(
        message: String,
        throwable: Throwable?,
        tag: String?,
    ) {
        Napier.w(
            message = message,
            throwable = throwable,
            tag = tag,
        )
    }

    override fun e(
        message: String,
        throwable: Throwable?,
        tag: String?,
    ) {
        Napier.e(
            message = message,
            throwable = throwable,
            tag = tag,
        )
    }
}
