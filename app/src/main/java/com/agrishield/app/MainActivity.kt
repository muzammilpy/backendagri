package com.agrishield.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.agrishield.app.domain.DiagnosisViewModel
import com.agrishield.app.ui.screens.*
import com.agrishield.app.ui.theme.AgriShieldTheme

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Camera  : Screen("camera",  "Diagnose", Icons.Default.Add)
    object Result  : Screen("result",  "Result",   Icons.Default.Star)
    object Map     : Screen("map",     "GIS Map",  Icons.Default.Place)
    object Weather : Screen("weather", "Weather",  Icons.Default.Refresh)
    object History : Screen("history", "History",  Icons.Default.Warning)
}

val bottomNavItems = listOf(Screen.Camera, Screen.Map, Screen.Weather, Screen.History)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgriShieldTheme { AgriShieldMainContent() }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgriShieldMainContent() {
    val navController = rememberNavController()
    val vm: DiagnosisViewModel = viewModel()

    LaunchedEffect(Unit) { vm.initTts() }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = androidx.compose.ui.graphics.Color(0xFF0A2419)) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDest = navBackStackEntry?.destination
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDest?.hierarchy?.any { it.route == screen.route } == true,
                        onClick  = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState    = true
                            }
                        },
                        icon  = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Screen.Camera.route,
            modifier         = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Camera.route) {
                CameraScreen(vm = vm, onResultReady = {
                    navController.navigate(Screen.Result.route)
                })
            }
            composable(Screen.Result.route) {
                ResultScreen(vm = vm, onBack = { navController.popBackStack() })
            }
            composable(Screen.Map.route)     { MapScreen(vm = vm) }
            composable(Screen.Weather.route) { WeatherScreen(vm = vm) }
            composable(Screen.History.route) { HistoryScreen(vm = vm) }
        }
    }
}
