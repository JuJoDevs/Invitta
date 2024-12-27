plugins {
    alias(libs.plugins.convention.android.library)
}

android {
    namespace = "com.jujodevs.invitta.library.logger.impl"
}

dependencies {
    implementation(projects.library.logger.api)
    implementation(libs.bundles.koin)
    implementation(libs.napier)
}
