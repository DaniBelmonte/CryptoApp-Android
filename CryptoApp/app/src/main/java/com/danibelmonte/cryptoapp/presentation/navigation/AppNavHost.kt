package com.danibelmonte.cryptoapp.presentation.navigation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.danibelmonte.cryptoapp.presentation.screen.CryptoCurrencyConverterScreen

/**
 * AppNavHost handles the main navigation flow of the application.
 *
 * Required configuration:
 *
 * 1. Set a valid `startDestination`.
 * 2. Adjust transitions if needed to match your design/UX requirements.
 * 3. Include the NavGraphs from installed modules. ← Important
 *    - Each module should expose its own navigation graph function and be registered here.
 * 4. Define screens using type-safe routes (e.g., a sealed class for `Screen` definitions).
 *
 * @param navController The shared navigation controller used across the app.
 */

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen, // Add start destination
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                ),
                initialOffsetX = { it }
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                ),
                initialOffsetX = { -it }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                ),
                targetOffsetX = { -it }
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                ),
                targetOffsetX = { it }
            )
        }
    ) {

        composable<Screen.MainScreen> {
            CryptoCurrencyConverterScreen()
        }
    }
}
