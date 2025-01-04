plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.test)
}

android {
    namespace = "com.jujodevs.invitta.library.authservice.impl"
}

dependencies {
    implementation(projects.library.authservice.api)
    implementation(projects.library.logger.api)
    implementation(projects.core.domain)
    implementation(libs.bundles.koin)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
}
