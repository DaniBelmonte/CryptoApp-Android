import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import rudo.com.convention.configureAndroidCompose
import rudo.com.convention.libs

/**
 * Adds Jetpack Compose configuration to Android library modules.
 *
 * Applies:
 * - Base library plugin (es-rudo-library)
 * - Compose compiler
 * - Compose setup (configureAndroidCompose)
 */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {

            //Apply required Gradle plugins
            pluginManager.run {
                apply(libs.findPlugin("es-rudo-library").get().get().pluginId)
                apply(libs.findPlugin("compose-compiler").get().get().pluginId)
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}