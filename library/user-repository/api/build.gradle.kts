plugins {
    alias(libs.plugins.convention.jvm.library)
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(projects.core.domain)
}
