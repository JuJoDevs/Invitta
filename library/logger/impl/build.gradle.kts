plugins {
    alias(libs.plugins.convention.jvm.library)
}

dependencies {
    implementation(projects.library.logger.api)
    implementation(libs.koin.core)
    implementation(libs.napier)
}
