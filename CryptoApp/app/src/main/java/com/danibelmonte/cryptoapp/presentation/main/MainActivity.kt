package com.danibelmonte.cryptoapp.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import com.danibelmonte.cryptoapp.presentation.navigation.AppNavHost
import com.danibelmonte.cryptoapp.presentation.ui.theme.MiAppTheme

/**
 * Main entry point of the application.
 * This activity is annotated with `@AndroidEntryPoint` to enable dependency injection with Hilt.
 *
 * 1. Create a custom theme.
 * 2. Set up your NavHost.
 * 3. Use the theme and the NavHost inside `setContent`.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()
            MiAppTheme {
                AppNavHost(navController = navHostController)
            }
        }
    }
}