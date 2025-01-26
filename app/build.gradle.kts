import java.util.Properties

plugins {
    alias(libs.plugins.convention.application.compose)
    alias(libs.plugins.convention.android.test)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
}

android {
    namespace = "com.jujodevs.invitta"

    signingConfigs {
        create("release") {
            val keystoreFile = project.rootProject.file("local.properties")
            val properties = Properties()
            properties.load(keystoreFile.inputStream())
            storeFile = file(properties.getProperty("KEYSTORE_STORE_FILE"))
            storePassword = properties.getProperty("KEYSTORE_STORE_PASSWORD")
            keyAlias = properties.getProperty("KEYSTORE_KEY_ALIAS")
            keyPassword = properties.getProperty("KEYSTORE_KEY_PASSWORD")
        }
    }

    defaultConfig {
        signingConfig = signingConfigs.getByName("release")
    }
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(projects.core.activityProvider.api)
    implementation(projects.core.activityProvider.impl)
    implementation(projects.core.coroutines)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designSystem)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.stringResources)
    implementation(projects.library.authservice.api)
    implementation(projects.library.authservice.impl)
    implementation(projects.library.googleauth.api)
    implementation(projects.library.googleauth.impl)
    implementation(projects.library.logger.api)
    implementation(projects.library.logger.impl)
    implementation(projects.library.remoteConfig.api)
    implementation(projects.library.remoteConfig.impl)
    implementation(projects.library.remoteDatabase.api)
    implementation(projects.library.remoteDatabase.impl)
}

tasks.named("preBuild")
    .configure {
        dependsOn(":copyGitHooks")
    }
