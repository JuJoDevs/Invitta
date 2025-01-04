plugins {
    alias(libs.plugins.convention.jvm.library)
}

dependencies {
    implementation(libs.koin.core)
    implementation(libs.coroutines.core)
}
