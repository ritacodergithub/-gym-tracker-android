package com.example.e_commerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.e_commerce.ui.navigation.GymNavHost
import com.example.e_commerce.ui.navigation.Screen
import com.example.e_commerce.ui.theme.GymTheme
import kotlinx.coroutines.flow.first

// Single-activity Compose app. Start destination is decided by the first
// UserProfile emission from Room: if null → onboarding, else → home.
// Subsequent profile changes (e.g. user resets from Settings) are handled
// by explicit navigation, so we only lock in the decision once.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val profileRepository = (application as GymApp).userProfileRepository

        setContent {
            GymTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var startRoute by remember { mutableStateOf<String?>(null) }
                    LaunchedEffect(Unit) {
                        val first = profileRepository.observeProfile().first()
                        startRoute = if (first == null) Screen.Onboarding.route
                        else Screen.Home.route
                    }
                    startRoute?.let { route ->
                        GymNavHost(startDestination = route)
                    }
                }
            }
        }
    }
}