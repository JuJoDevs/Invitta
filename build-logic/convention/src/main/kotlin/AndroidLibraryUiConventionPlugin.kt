import com.jujodevs.invitta.buildlogic.convention.addUiLayerDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryUiConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.jujodevs.invitta.convention.android.library.compose")
            }

            dependencies {
                addUiLayerDependencies(target)
            }
        }
    }
}
