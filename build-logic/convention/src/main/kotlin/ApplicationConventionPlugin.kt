@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.ApplicationExtension
import com.jujodevs.invitta.buildlogic.convention.configureAppBuildTypes
import com.jujodevs.invitta.buildlogic.convention.configureKotlinAndroid
import com.jujodevs.invitta.buildlogic.convention.getTargetSdk
import com.jujodevs.invitta.buildlogic.convention.getVersionCode
import com.jujodevs.invitta.buildlogic.convention.getVersionName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                configureAppBuildTypes(this)
                defaultConfig {
                    targetSdk = getTargetSdk()
                    versionCode = getVersionCode()
                    versionName = getVersionName()
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }
            }
        }
    }
}
