plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.test)
}

android {
    namespace = "com.jujodevs.invitta.library.googleauth.impl"
}

dependencies {
    implementation(projects.library.googleauth.api)
    implementation(projects.library.remoteConfig.api)
    implementation(projects.library.logger.api)
    implementation(projects.core.activityProvider.api)
    implementation(projects.core.coroutines)
    implementation(projects.core.domain)
    implementation(libs.bundles.koin)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
}
