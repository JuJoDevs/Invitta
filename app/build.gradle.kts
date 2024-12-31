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

    implementation(projects.core.designSystem)
    implementation(projects.core.stringResources)
    implementation(projects.library.logger.api)
    implementation(projects.library.logger.impl)
    implementation(projects.library.remoteConfig.api)
    implementation(projects.library.remoteConfig.impl)
}

tasks.named("preBuild")
    .configure {
        dependsOn(":copyGitHooks")
    }
