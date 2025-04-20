import com.jujodevs.invitta.buildlogic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class JvmTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.withType<Test> {
                useJUnitPlatform()
                testLogging {
                    events("passed", "skipped", "failed")
                }
            }

            dependencies {
                "testImplementation"(project(":core:testing"))
                "testImplementation"(platform(libs.findLibrary("junit.bom").get()))
                "testImplementation"(libs.findLibrary("junit.jupiter.api").get())
                "testImplementation"(libs.findLibrary("junit.jupiter.params").get())
                "testRuntimeOnly"(libs.findLibrary("junit.jupiter.engine").get())
                "testRuntimeOnly"(libs.findLibrary("junit.platform.launcher").get())
                "testImplementation"(libs.findLibrary("mockk").get())
                "testImplementation"(libs.findLibrary("kluent").get())
                "testImplementation"(libs.findLibrary("kotest").get())
                "testImplementation"(libs.findLibrary("turbine").get())
                "testImplementation"(libs.findLibrary("koin.test").get())
                "testImplementation"(libs.findLibrary("koin.test.junit5").get())
                "testImplementation"(libs.findLibrary("kotlinx.coroutines.test").get())
            }
        }
    }
}
