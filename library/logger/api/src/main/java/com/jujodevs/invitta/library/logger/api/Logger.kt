package com.jujodevs.invitta.library.logger.api

interface Logger {
    fun d(
        message: String,
        throwable: Throwable? = null,
        tag: String? = null,
    )

    fun i(
        message: String,
        throwable: Throwable? = null,
        tag: String? = null,
    )

    fun w(
        message: String,
        throwable: Throwable? = null,
        tag: String? = null,
    )

    fun e(
        message: String,
        throwable: Throwable? = null,
        tag: String? = null,
    )
}
