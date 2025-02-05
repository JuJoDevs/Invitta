plugins {
    alias(libs.plugins.convention.jvm.library)
    alias(libs.plugins.convention.jvm.test)
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.koin.core)
    implementation(projects.core.domain)
    implementation(projects.library.userRepository.api)
    implementation(projects.library.authservice.api)
    implementation(projects.library.remoteDatabase.api)
}
