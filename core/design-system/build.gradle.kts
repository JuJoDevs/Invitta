plugins {
    alias(libs.plugins.convention.android.library.compose)
}

android {
    namespace = "com.jujodevs.invitta.core.designsystem"
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.material3)

    implementation(projects.core.stringResources)
}
