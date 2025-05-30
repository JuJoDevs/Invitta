import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.PosixFilePermissions

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.paparazzi) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

subprojects {
    apply {
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    ktlint {
        android.set(true)
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.HTML)
        }
        filter {
            exclude("**/generated/**")
        }
    }
}

detekt {
    val filesProp = project.findProperty("detektFiles") as String?
    if (!filesProp.isNullOrBlank()) {
        val fileList =
            filesProp.split(",")
                .filter { it.isNotBlank() }
                .map { file(it) }
        source.setFrom(fileList)
    } else {
        source.setFrom(files("src/main/kotlin"))
    }
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        //noinspection UseTomlInstead
        detektPlugins("io.nlopez.compose.rules:detekt:0.4.22")
    }
}

tasks.register("copyGitHooks") {
    doLast {
        val gitDir = file(".git")
        if (!gitDir.exists()) {
            logger.warn(".git directory not found, skipping git hooks copy.")
            return@doLast
        }

        val sourceDir = file("hooks")
        val targetDir = file(".git/hooks")
        sourceDir.listFiles()
            ?.forEach { sourceFile ->
                val targetFile = File(targetDir, sourceFile.name)
                if (!targetFile.exists() ||
                    Files.mismatch(sourceFile.toPath(), targetFile.toPath()) != -1L
                ) {
                    Files.copy(
                        sourceFile.toPath(),
                        targetFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING,
                    )
                    print("> Copied hook: ${sourceFile.name}")
                }
            }
    }
    doLast {
        val gitDir = file(".git")
        if (!gitDir.exists()) return@doLast

        file(".git/hooks/").walk()
            .forEach { file ->
                if (file.isFile) {
                    try {
                        Files.setPosixFilePermissions(
                            file.toPath(),
                            PosixFilePermissions.fromString("rwxr-xr-x"),
                        )
                    } catch (_: UnsupportedOperationException) {
                        logger.warn("Unable to set POSIX permissions on ${file.name}.")
                    }
                }
            }
    }
}
