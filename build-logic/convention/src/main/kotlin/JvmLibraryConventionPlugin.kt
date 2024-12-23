import com.jujodevs.invitta.buildlogic.convention.configureKotlinJvm
import com.jujodevs.invitta.buildlogic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
            }

            configureKotlinJvm()

            dependencies {
                add("implementation", libs.findLibrary("napier").get())
            }
        }
    }
}
