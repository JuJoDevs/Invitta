plugins {
    alias(libs.plugins.convention.android.library)
}

android {
    namespace = "com.jujodevs.invitta.library.remoteconfig.impl"
}

dependencies {
    implementation(projects.library.remoteConfig.api)

    implementation(libs.bundles.koin)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.remote.config)
}
