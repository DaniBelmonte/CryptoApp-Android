import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import rudo.com.convention.libs

/**
 * Adds dependencies for both unit tests and Android instrumentation tests.
 *
 *Bundles applied:
 * - `test`: JUnit, Kotlin test, MockK, Turbine, Coroutine test, Truth
 * - `instrumentationTest`: Espresso, Compose UI test, JUnit extensions, Hilt testing, etc.
 */
class AndroidTestConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            dependencies {
                "testImplementation"(libs.findBundle("test").get())
                "androidTestImplementation"(libs.findBundle("instrumentationTest").get())
                "debugImplementation"(libs.findLibrary("androidx-ui-test-manifest").get())
            }
        }
    }
}