plugins {
    alias(libs.plugins.es.rudo.application.compose)
    alias(libs.plugins.es.rudo.compose.ui)
    alias(libs.plugins.es.rudo.dependency.injection)
}

/** Common Android configurations are already handled by the convention plugins.
 * If module-specific configuration is needed, for example, for app signing (signingConfigs),
 * add it here. */
android {
    namespace = "com.danibelmonte.cryptoapp" // Replace with your actual namespace
}

/** The convention plugins (`es.rudo.*`) already provide:
 *   All necessary Jetpack Compose dependencies (UI, Material3, Navigation, Activity, etc.)
 *   Compose compiler plugin.
 *   For dependency injection Hilt and KSP.
 *   Kotlinx Serialization JSON.
 *
 * Add modules or other app-specific dependencies.
 *
 * Example (Room):
 * dependencies {
 *     implementation(project(":profile:))
 *     implementation(libs.androidx.room.ktx)
 *     ksp(libs.androidx.room.compiler)
 * }
 */
