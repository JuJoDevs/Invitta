import java.util.Properties

plugins {
    alias(libs.plugins.convention.application.compose)
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

tasks.named("preBuild")
    .configure {
        dependsOn(":copyGitHooks")
    }
