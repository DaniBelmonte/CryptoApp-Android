import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import rudo.com.convention.libs

/**
 * Adds Compose UI libraries and lifecycle support for Compose-based UIs.
 *
 * Bundles applied:
 * - `compose`: Material, UI, Navigation, etc.
 * - `compose-debug`: Tooling, UI Preview, Manifest support
 * - `lifecycle`: Lifecycle runtime, ViewModel support for Compose
 */
class AndroidComposeUiConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            dependencies {
                "implementation"(libs.findBundle("compose").get())
                "implementation"(libs.findBundle("compose-debug").get())
                "implementation"(libs.findBundle("lifecycle").get())
            }
        }
    }
}