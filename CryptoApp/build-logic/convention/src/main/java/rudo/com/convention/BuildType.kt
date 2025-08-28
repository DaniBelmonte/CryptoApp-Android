package rudo.com.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Configures `debug` and `release` build types for either an Application or Library module.
 * - Enables `buildConfig` generation
 * - Applies environment constants (e.g., API base URL)
 * - Sets ProGuard configuration for release builds
 *
 * @param extensionType Enum to distinguish between app and library module behavior
 */
internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }
    }

    when (extensionType) {
        ExtensionType.APPLICATION -> {
            extensions.configure<ApplicationExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType()
                    }
                    release {
                        configureReleaseBuildType(commonExtension)
                    }
                }
            }
        }

        ExtensionType.LIBRARY -> {
            extensions.configure<LibraryExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType()
                    }
                    release {
                        configureReleaseBuildType(commonExtension)
                    }
                }
            }
        }
    }
}

//Debug build type configuration:
private fun BuildType.configureDebugBuildType() {
    buildConfigField("String", "API_BASE_URL", "\"https://gula.rudo.es/\"")
}

//Release build type configuration:
private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    isMinifyEnabled = false
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
    buildConfigField("String", "API_BASE_URL", "\"https://gula.rudo.es/\"")
}

//Type of module for which build types will be configured.
enum class ExtensionType {
    APPLICATION,
    LIBRARY
}