plugins {
    alias(libs.plugins.convention.android.library.ui)
}

android {
    namespace = "com.jujodevs.invitta.feature.home.impl"
}

dependencies {
    implementation(projects.core.presentation.stringResources)
    implementation(projects.core.presentation.ui)
}
