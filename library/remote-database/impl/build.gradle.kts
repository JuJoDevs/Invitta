plugins {
    alias(libs.plugins.convention.android.library)
    alias(libs.plugins.convention.jvm.test)
}

android {
    namespace = "com.jujodevs.invitta.library.remotedatabase.impl"
}

dependencies {
    implementation(projects.library.remoteDatabase.api)
    implementation(projects.library.logger.api)
    implementation(projects.core.coroutines)
    implementation(projects.core.domain)
    implementation(libs.bundles.koin)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
}
