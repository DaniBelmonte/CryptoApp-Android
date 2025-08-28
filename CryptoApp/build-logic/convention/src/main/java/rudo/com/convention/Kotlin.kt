package rudo.com.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * Applies common Kotlin and Android settings:
 * - Sets compile SDK and min SDK
 * - Configures Kotlin to use JVM toolchain version
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {

    commonExtension.apply {
        compileSdk = 35
        defaultConfig.minSdk = 31

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }

    extensions.findByType(KotlinJvmProjectExtension::class.java)?.apply {
        jvmToolchain(21)
    }

    // Needed to support Java 21 language features on lower API levels
    dependencies {
        "coreLibraryDesugaring"(libs.findLibrary("desugar.jdk.libs").get())
    }

}

