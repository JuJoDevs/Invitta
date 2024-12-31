plugins {
    alias(libs.plugins.convention.jvm.library)
    alias(libs.plugins.convention.jvm.test)
}

dependencies {
    implementation(projects.library.logger.api)
    implementation(libs.koin.core)
    implementation(libs.napier)
}
