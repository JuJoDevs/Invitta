package com.jujodevs.invitta.library.authservice.impl.di

import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.jujodevs.invitta.library.authservice.api.AuthService
import com.jujodevs.invitta.library.authservice.impl.SupabaseAuthService
import org.koin.dsl.module

val authServiceModule =
    module {
        single<FirebaseAuth> { FirebaseAuth.getInstance() }
        single<CredentialManager> { CredentialManager.create(get()) }
        single<AuthService> { SupabaseAuthService(get(), get()) }
    }
