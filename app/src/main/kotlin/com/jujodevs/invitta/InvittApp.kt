package com.jujodevs.invitta

import android.app.Application
import com.jujodevs.invitta.di.appModule
import com.jujodevs.invitta.library.logger.impl.loggerModule
import com.jujodevs.invitta.library.remoteconfig.impl.di.remoteConfigModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class InvittApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@InvittApp)
            modules(
                appModule,
                loggerModule,
                remoteConfigModule,
            )
        }
    }
}
