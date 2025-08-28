import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import rudo.com.convention.ExtensionType
import rudo.com.convention.configureBuildTypes
import rudo.com.convention.configureKotlinAndroid
import rudo.com.convention.libs

/**
 * Applies the base configuration for Android application modules.
 *
 * Includes:
 * - Gradle plugins: Android, Kotlin, Hilt, Serialization, KSP
 * - Application-level configuration
 * - Kotlin and build type config
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {

            //Apply required Gradle plugins
            pluginManager.run {
                apply(
                    libs.findPlugin("android-application").get().get().pluginId
                )
                apply(libs.findPlugin("org-jetbrains-kotlin-android").get().get().pluginId)
                apply(libs.findPlugin("hilt-android").get().get().pluginId)
                apply(
                    libs.findPlugin("jetbrains-kotlin-plugin-serialization").get()
                        .get().pluginId
                )
                apply(libs.findPlugin("ksp").get().get().pluginId)
            }

            //Configure Android Application
            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = "com.danibelmonte.cryptoapp"
                    targetSdk = 35
                    versionCode = 1
                    versionName = "1.0.0"

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    vectorDrawables {
                        useSupportLibrary = true
                    }
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }

                configureKotlinAndroid(commonExtension = this)


                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.APPLICATION
                )

                dependencies {
                    "implementation"(libs.findLibrary("androidx-core-ktx").get())
                }
            }
        }
    }
}