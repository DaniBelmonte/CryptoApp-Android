plugins {
    `kotlin-dsl`
}

group = "es.rudo.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    // All convention plugins need to configure here and in libs to match refer when use in build gradle plugin.
    plugins{
        register("androidApplication") {
            id = "es.rudo.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "es.rudo.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidLibrary") {
            id = "es.rudo.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "es.rudo.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidComposeUi") {
            id = "es.rudo.compose.ui"
            implementationClass = "AndroidComposeUiConventionPlugin"
        }

        register("androidTest") {
            id = "es.rudo.unit.test"
            implementationClass = "AndroidTestConventionPlugin"
        }

        register("androidNetworkCoroutine") {
            id = "es.rudo.kotlin.network.coroutine"
            implementationClass = "AndroidNetworkCoroutinesConventionPlugin"
        }

        register("androidDependencyInjection") {
            id = "es.rudo.dependency.injection"
            implementationClass = "AndroidDependencyInjectionConventionPlugin"
        }
    }
}