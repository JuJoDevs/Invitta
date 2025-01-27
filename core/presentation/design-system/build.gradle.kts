plugins {
    alias(libs.plugins.convention.android.library.compose)
    alias(libs.plugins.convention.android.test)
    alias(libs.plugins.convention.paparazzi)
}

android {
    namespace = "com.jujodevs.invitta.core.presentation.designsystem"
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.material3)

    implementation(projects.core.presentation.stringResources)
}
