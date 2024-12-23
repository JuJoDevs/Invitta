import com.jujodevs.invitta.buildlogic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.jujodevs.invitta.convention.jvm.test")
            }
            dependencies {
                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("hamcrest").get())
                "testImplementation"(libs.findLibrary("junit.jupiter.vintage.engine").get())
                "testImplementation"(libs.findLibrary("robolectric").get())
                "testImplementation"(libs.findLibrary("androidx.compose.ui.test.junit4").get())
                "testImplementation"(libs.findLibrary("koin.test.junit4").get())
                "androidTestImplementation"(project(":core:testing"))
                "androidTestImplementation"(libs.findLibrary("androidx.junit").get())
                "androidTestImplementation"(libs.findLibrary("androidx.espresso.core").get())
                "androidTestImplementation"(libs.findLibrary("androidx.compose.ui.test.junit4").get())
                "androidTestImplementation"(libs.findLibrary("koin.test").get())
                "androidTestImplementation"(libs.findLibrary("koin.test.junit5").get())
            }
        }
    }
}
