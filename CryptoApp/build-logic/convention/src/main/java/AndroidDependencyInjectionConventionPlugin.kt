import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import rudo.com.convention.libs

/**
 * Configures Dependency Injection for modules.
 *
 *  Plugins applied:
 *  - Hilt Android Gradle plugin (via `libs.plugins.hilt.android`)
 *  - KSP (Kotlin Symbol Processing) plugin (via `libs.plugins.ksp`)
 *
 * Bundles applied:
 * - `dependencyInjection`: Hilt, Compiler, etc.
 */
class AndroidDependencyInjectionConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(libs.findPlugin("hilt-android").get().get().pluginId)
                apply(libs.findPlugin("ksp").get().get().pluginId)
            }

            dependencies {
                "implementation"(libs.findBundle("dependencyInjection").get())
                "ksp"(libs.findLibrary("hilt-android-compiler").get())
            }
        }
    }
}