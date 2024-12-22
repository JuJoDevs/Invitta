import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.jujodevs.invitta.buildlogic.convention"

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaVersion.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaVersion.get())
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.javaVersion.get()))
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("application") {
            id = "com.jujodevs.invitta.convention.application"
            implementationClass = "ApplicationConventionPlugin"
        }
        register("application-compose") {
            id = "com.jujodevs.invitta.convention.application.compose"
            implementationClass = "ApplicationComposeConventionPlugin"
        }
        register("android-library") {
            id = "com.jujodevs.invitta.convention.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("android-library-compose") {
            id = "com.jujodevs.invitta.convention.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("android-library-ui") {
            id = "com.jujodevs.invitta.convention.android.library.ui"
            implementationClass = "AndroidLibraryUiConventionPlugin"
        }
        register("jvm-library") {
            id = "com.jujodevs.invitta.convention.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
