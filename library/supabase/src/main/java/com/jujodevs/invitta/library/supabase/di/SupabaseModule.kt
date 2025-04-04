package com.jujodevs.invitta.library.supabase.di

import com.jujodevs.invitta.library.supabase.data.SupabaseClientFactory
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import org.koin.dsl.module

val supabaseModule =
    module {
        single<SupabaseClient> { SupabaseClientFactory(get(), get()).create() }
        single<Auth> { get<SupabaseClient>().auth }
    }
