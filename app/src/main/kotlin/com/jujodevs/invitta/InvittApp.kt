package com.jujodevs.invitta

import android.app.Application
import com.jujodevs.invitta.core.activityprovider.impl.di.activityProviderModule
import com.jujodevs.invitta.core.coroutines.coroutinesModule
import com.jujodevs.invitta.di.appModule
import com.jujodevs.invitta.feature.home.impl.di.homeModule
import com.jujodevs.invitta.library.authservice.impl.di.authServiceModule
import com.jujodevs.invitta.library.googleauth.impl.di.googleAuthModule
import com.jujodevs.invitta.library.logger.impl.loggerModule
import com.jujodevs.invitta.library.remoteconfig.impl.di.remoteConfigModule
import com.jujodevs.invitta.library.remotedatabase.impl.di.remoteDatabaseModule
import com.jujodevs.invitta.library.userRepository.impl.di.userModule
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
                activityProviderModule,
                authServiceModule,
                appModule,
                coroutinesModule,
                googleAuthModule,
                homeModule,
                loggerModule,
                remoteConfigModule,
                remoteDatabaseModule,
                userModule,
            )
        }
    }
}
