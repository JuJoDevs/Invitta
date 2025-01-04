package com.jujodevs.invitta.library.logger.impl

import com.jujodevs.invitta.core.testing.verifyOnce
import io.github.aakira.napier.Napier
import io.mockk.justRun
import io.mockk.mockkObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultLoggerTest {
    private lateinit var logger: DefaultLogger

    @BeforeEach
    fun setup() {
        mockkObject(Napier)
        justRun { Napier.d(any<String>(), any(), any()) }
        justRun { Napier.i(any<String>(), any(), any()) }
        justRun { Napier.w(any<String>(), any(), any()) }
        justRun { Napier.e(any<String>(), any(), any()) }
        logger = DefaultLogger()
    }

    @Test
    fun `GIVEN debug message WHEN d is called THEN logs debug message`() {
        val message = "Debug message"
        val throwable = Throwable("Debug throwable")
        val tag = "DebugTag"

        logger.d(message, throwable, tag)

        verifyOnce { Napier.d(message, throwable, tag) }
    }

    @Test
    fun `GIVEN info message WHEN i is called THEN logs info message`() {
        val message = "Info message"
        val throwable = null
        val tag = "InfoTag"

        logger.i(message, throwable, tag)

        verifyOnce { Napier.i(message, throwable, tag) }
    }

    @Test
    fun `GIVEN warning message WHEN w is called THEN logs warning message`() {
        val message = "Warning message"
        val throwable = Throwable("Warning throwable")
        val tag = "WarningTag"

        logger.w(message, throwable, tag)

        verifyOnce { Napier.w(message, throwable, tag) }
    }

    @Test
    fun `GIVEN error message WHEN e is called THEN logs error message`() {
        val message = "Error message"
        val throwable = Throwable("Error throwable")
        val tag = null

        logger.e(message, throwable, tag)

        verifyOnce { Napier.e(message, throwable, tag) }
    }
}
