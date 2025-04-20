plugins {
    alias(libs.plugins.convention.jvm.library)
}

dependencies {
    implementation(platform(libs.junit.bom))
    implementation(libs.junit)
    implementation(libs.junit.jupiter.api)
    implementation(libs.kotest)
    implementation(libs.mockk)
    implementation(libs.kotlinx.coroutines.test)
}
