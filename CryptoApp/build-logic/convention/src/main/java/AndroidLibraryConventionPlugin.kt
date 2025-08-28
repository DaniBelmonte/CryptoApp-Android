import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import rudo.com.convention.ExtensionType
import rudo.com.convention.configureBuildTypes
import rudo.com.convention.configureKotlinAndroid
import rudo.com.convention.libs

/**
 * Applies base configuration for Android library modules.
 *
 * Includes:
 * - Plugins: android-library, kotlin-android, serialization
 * - Library-level configuration
 * - Kotlin and build type config
 */
class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {

            //Apply required Gradle plugins
            pluginManager.run {
                apply(libs.findPlugin("android-library").get().get().pluginId)
                apply(libs.findPlugin("org-jetbrains-kotlin-android").get().get().pluginId)
                apply(
                    libs.findPlugin("jetbrains-kotlin-plugin-serialization").get()
                        .get().pluginId
                )
            }

            //Configure Android Library
            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(commonExtension = this)
                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.LIBRARY
                )
                buildFeatures {
                    buildConfig = true
                }
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
            }

            dependencies {
                "testImplementation"(kotlin("test"))
                "implementation"(libs.findLibrary("androidx-core-ktx").get())
            }
        }
    }
}