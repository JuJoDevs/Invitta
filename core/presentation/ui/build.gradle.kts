plugins {
    alias(libs.plugins.convention.android.library.compose)
}

android {
    namespace = "com.jujodevs.invitta.core.presentation.ui"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.presentation.stringResources)
    implementation(libs.bundles.compose)
}
