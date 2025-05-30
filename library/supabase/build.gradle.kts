plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.jujodevs.invitta.library.supabase"
}

dependencies {
    implementation(projects.library.remoteConfig.api)
    implementation(libs.bundles.koin)
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.auth)
    implementation(libs.supabase.postgrest)
    implementation(libs.ktor.client.android)
}
