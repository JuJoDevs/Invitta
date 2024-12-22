import com.android.build.api.dsl.ApplicationExtension
import com.jujodevs.invitta.buildlogic.convention.addUiLayerDependencies
import com.jujodevs.invitta.buildlogic.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class ApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.jujodevs.invitta.convention.application")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            extensions.configure<ApplicationExtension> {
                configureAndroidCompose(this)
            }
            dependencies {
                addUiLayerDependencies(target)
            }
        }
    }
}
