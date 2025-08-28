# 📘 Gradle Convention Plugins

Enables a clean, scalable, and maintainable project configuration.

---

## 🛠️ Where is everything configured?

- **SDK and JDK versions**  
  Configured inside the `configureKotlinAndroid()` function.

- **Build types (debug/release)**  
  Managed by the `configureBuildTypes()` function.

- **Jetpack Compose support**  
  Enabled via the `configureAndroidCompose()` function.

- **Shared plugins and configuration**  
  Located in the `convention/` folder.

- **Common dependencies**  
  Defined in the `libs.versions.toml` file and grouped using bundles.

---

# 🚀 Integrating Gradle Convention Plugins into an Existing Project

This guide explains how to add and configure the Convention Plugins in a pre-existing Android project.

---

## 📦 `libs.versions.toml` Configuration

1. **Review the `libs.versions.toml` file** from the [Gula repository](gradle/libs.versions.toml).
2. **Make sure the bundles and dependencies are present**:

### ✅ Required dependencies:

```toml
# Libraries
desugar-jdk-libs = { module = "com.android.tools:desugar_jdk_libs", version.ref = "desugar_jdk_libs" }

# Gradle plugins
android-gradlePlugin = { group = "com.android.tools.build", name = "gradle", version.ref = "agp" }
android-tools-common = { group = "com.android.tools", name = "common", version.ref = "androidTools" }
kotlin-gradlePlugin = { group = "org.jetbrains.kotlin", name = "kotlin-gradle-plugin", version.ref = "kotlin" }
ksp-gradlePlugin = { group = "com.google.devtools.ksp", name = "com.google.devtools.ksp.gradle.plugin", version.ref = "ksp" }
room-gradlePlugin = { group = "androidx.room", name = "room-gradle-plugin", version.ref = "roomCommon" }

# Convention Plugins
es-rudo-application = { id = "es.rudo.application", version = "unspecified" }
es-rudo-application-compose = { id = "es.rudo.application.compose", version = "unspecified" }
es-rudo-library = { id = "es.rudo.library", version = "unspecified" }
es-rudo-library-compose = { id = "es.rudo.library.compose", version = "unspecified" }
es-rudo-test = { id = "es.rudo.unit.test", version = "unspecified" }
es-rudo-compose-ui = { id = "es.rudo.compose.ui", version = "unspecified" }
es-rudo-network-coroutines = { id = "es.rudo.kotlin.network.coroutine", version = "unspecified" }
es-rudo-dependency-injection = { id = "es.rudo.dependency.injection", version = "unspecified" }

test = [
  "junit",
  "mockk",
  "kotlin-test-junit",
  "kotlinx-coroutines-test",
  "androidx-junit",
]
instrumentationTest = [
  "androidx-espresso-core",
  "androidx-ui-test-junit4",
  "androidx-ui-test-manifest"
]
coroutines = [
  "kotlinx-coroutines-android",
  "kotlinx-coroutines-core"
]
networking = [
  "retrofit",
  "logging-interceptor",
  "gson",
  "moshimoshi",
]
dependencyInjection = [
  "hilt-android",
  "androidx-hilt-navigation-compose"
]

```
3. Sync the project. If it fails, verify that the aliases in libs.versions.toml match your project usage.
4. In the root project, open settings.gradle.kts and add the following line in pluginManagement:  **includeBuild("build-logic")**
5. Apply the appropriate convention plugin

---

## 🧩 Adding a New Module

1. Create the module as usual.
2. Apply the appropriate convention plugin in the module’s `build.gradle.kts` file:

   ```kotlin
   plugins {
       alias("libs.plugins.es.rudo.library")             // For standard library
       alias("libs.plugins.es.rudo.library.compose")     // For Compose-based library
       alias("libs.plugins.es.rudo.application")         // For application module
       alias("libs.plugins.es.rudo.application.compose") // For Compose-based application
       alias("libs.plugins.es.rudo.compose.ui") // For compose dependencies
       alias("libs.plugins.es.rudo.network.coroutines") // For network dependencies
       alias("libs.plugins.es.rudo.dependency.injection") // For dependency injection dependencies
       alias("libs.plugins.es.rudo.unit.test") // For unit test dependencies
   }
   ```

