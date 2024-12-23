@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.LibraryExtension
import com.jujodevs.invitta.buildlogic.convention.configureKotlinAndroid
import com.jujodevs.invitta.buildlogic.convention.configureLibraryBuildTypes
import com.jujodevs.invitta.buildlogic.convention.getTargetSdk
import com.jujodevs.invitta.buildlogic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureLibraryBuildTypes(this)
                testOptions {
                    targetSdk = getTargetSdk()
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                }
                lint {
                    targetSdk = getTargetSdk()
                }
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }

            dependencies {
                add("implementation", libs.findLibrary("androidx-core-ktx").get())
                add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
                add("implementation", libs.findLibrary("napier").get())
            }
        }
    }
}
