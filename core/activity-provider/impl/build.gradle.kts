plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.android.test)
}

android {
    namespace = "com.jujodevs.invitta.core.activityprovider.impl"
}

dependencies {
    implementation(projects.core.activityProvider.api)
    implementation(libs.bundles.koin)
}
