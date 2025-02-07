plugins {
    alias(libs.plugins.convention.android.library.ui)
    alias(libs.plugins.convention.jvm.test)
}

android {
    namespace = "com.jujodevs.invitta.feature.home.impl"
}

dependencies {
    implementation(projects.core.coroutines)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designSystem)
    implementation(projects.core.presentation.stringResources)
    implementation(projects.core.presentation.ui)
    implementation(projects.library.remoteDatabase.api)
    implementation(projects.library.userRepository.api)
}
