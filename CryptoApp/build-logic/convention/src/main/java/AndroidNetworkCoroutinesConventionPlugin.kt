import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import rudo.com.convention.libs

/**
 * Adds dependencies related to Kotlin Coroutines and networking libraries (e.g. Retrofit, OkHttp).
 *
 * Bundles applied:
 * - `coroutines`: Core Kotlin coroutines, including Android dispatcher and flow
 * - `networking`: Retrofit, OkHttp, logging interceptor, etc.
 */
class AndroidNetworkCoroutinesConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            dependencies {
                "implementation"(libs.findBundle("coroutines").get())
                "implementation"(libs.findBundle("networking").get())
            }
        }
    }
}