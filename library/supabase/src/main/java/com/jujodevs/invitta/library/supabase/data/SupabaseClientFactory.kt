package com.jujodevs.invitta.library.supabase.data

import android.content.Context
import com.jujodevs.invitta.library.remoteconfig.api.RemoteConfigName
import com.jujodevs.invitta.library.remoteconfig.api.RemoteConfigProvider
import com.russhwolf.settings.SharedPreferencesSettings
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.SettingsSessionManager
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

internal class SupabaseClientFactory(
    private val context: Context,
    private val remoteConfigProvider: RemoteConfigProvider,
) {
    fun create(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl =
                remoteConfigProvider
                    .getStringParameter(RemoteConfigName.SUPABASE_URL),
            supabaseKey =
                remoteConfigProvider
                    .getStringParameter(RemoteConfigName.SUPABASE_ANON_KEY),
        ) {
            install(Auth) {
                sessionManager =
                    SettingsSessionManager(
                        settings =
                            SharedPreferencesSettings(
                                context.getSharedPreferences(
                                    SUPABASE_SETTINGS,
                                    Context.MODE_PRIVATE,
                                ),
                            ),
                    )
            }
            install(Postgrest)
        }
    }
}

private const val SUPABASE_SETTINGS = "supabase_settings"
