import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import rudo.com.convention.configureAndroidCompose
import rudo.com.convention.libs

/**
 * Adds Jetpack Compose configuration for Android application modules.
 *
 * Applies:
 * - Base app plugin (es-rudo-application)
 * - Compose compiler
 * - Compose setup (configureAndroidCompose)
 */
class AndroidApplicationComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {

            //Apply required Gradle plugins
            pluginManager.run {
                apply(libs.findPlugin("es-rudo-application").get().get().pluginId)
                apply(libs.findPlugin("compose-compiler").get().get().pluginId)
            }

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}