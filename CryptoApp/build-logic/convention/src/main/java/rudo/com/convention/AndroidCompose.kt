package rudo.com.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Enables and configures Jetpack Compose support for the given Android module.
 * - Enables Compose in build features
 * - Sets Compose Compiler extension version using the Kotlin libs version
 * - Adds base Compose dependencies:
 *     - Compose BOM for dependency alignment
 *     - Tooling preview for debug builds
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.run {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.findVersion("kotlin").get().toString()
        }

        // Base Compose dependencies
        dependencies {
            val bom = libs.findLibrary("androidx.compose.bom").get()
            "implementation"(platform(bom))
            "androidTestImplementation"(platform(bom))
            "debugImplementation"(libs.findLibrary("androidx-ui-tooling-preview").get())
        }
    }
}