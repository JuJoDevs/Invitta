import java.util.Properties

plugins {
    alias(libs.plugins.convention.application.compose)
    alias(libs.plugins.convention.android.test)
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
    implementation(projects.library.logger.api)
    implementation(projects.library.logger.impl)
}

tasks.named("preBuild")
    .configure {
        dependsOn(":copyGitHooks")
    }
