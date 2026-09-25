package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.data.worker.WeatherSyncManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.model.AppLanguage
import com.example.model.computeInitials
import com.example.ui.components.EditProfileDialog
import com.example.ui.components.LanguageSelectorDialog
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.CropHealthViewModel
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage

enum class AppNavigationDestination(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    DIAGNOSIS(
        route = "diagnosis",
        title = "Diagnose",
        selectedIcon = Icons.Default.HealthAndSafety,
        unselectedIcon = Icons.Outlined.HealthAndSafety,
        testTag = "nav_diagnosis"
    ),
    IPM_ADVISOR(
        route = "ipm_advisor",
        title = "IPM Advisor",
        selectedIcon = Icons.Default.AccountTree,
        unselectedIcon = Icons.Outlined.AccountTree,
        testTag = "nav_ipm_advisor"
    ),
    HOTSPOTS(
        route = "hotspots",
        title = "Hotspots",
        selectedIcon = Icons.Default.Radar,
        unselectedIcon = Icons.Outlined.Radar,
        testTag = "nav_hotspots"
    ),
    FORECASTING(
        route = "forecasting",
        title = "Forecast",
        selectedIcon = Icons.Default.CloudQueue,
        unselectedIcon = Icons.Outlined.CloudQueue,
        testTag = "nav_forecasting"
    ),
    EXTENSION(
        route = "extension",
        title = "Extension",
        selectedIcon = Icons.Default.VerifiedUser,
        unselectedIcon = Icons.Outlined.VerifiedUser,
        testTag = "nav_extension"
    ),
    DASHBOARD(
        route = "dashboard",
        title = "Officials",
        selectedIcon = Icons.Default.Analytics,
        unselectedIcon = Icons.Outlined.Analytics,
        testTag = "nav_dashboard"
    );

    fun getLocalizedTitle(lang: AppLanguage): String = when (this) {
        DIAGNOSIS -> AppStrings.navDiagnosis(lang)
        IPM_ADVISOR -> AppStrings.navIpmAdvisor(lang)
        HOTSPOTS -> AppStrings.navHotspots(lang)
        FORECASTING -> AppStrings.navForecasting(lang)
        EXTENSION -> AppStrings.navExtension(lang)
        DASHBOARD -> AppStrings.navDashboard(lang)
    }
}

class MainActivity : ComponentActivity() {
    private val viewModel: CropHealthViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Schedule periodic background WorkManager to refresh microclimate weather daily asynchronously
        lifecycleScope.launch(Dispatchers.IO) {
            WeatherSyncManager.scheduleDailyWeatherSync(applicationContext)
        }

        setContent {
            MyApplicationTheme {
                var isSplashVisible by remember { mutableStateOf(true) }
                var currentDestination by remember { mutableStateOf(AppNavigationDestination.DIAGNOSIS) }
                var showLanguageDialog by remember { mutableStateOf(false) }
                var showProfileDialog by remember { mutableStateOf(false) }
                val selectedLanguage by viewModel.selectedLanguage.collectAsState()
                val userProfile by viewModel.userProfile.collectAsState()
                val dynamicInitials = remember(userProfile.name) { computeInitials(userProfile.name) }
                val visibleDestinations = remember(userProfile.staffUnlocked) {
                    if (userProfile.staffUnlocked) {
                        AppNavigationDestination.values().toList()
                    } else {
                        listOf(
                            AppNavigationDestination.DIAGNOSIS,
                            AppNavigationDestination.IPM_ADVISOR,
                            AppNavigationDestination.HOTSPOTS,
                            AppNavigationDestination.FORECASTING
                        )
                    }
                }

                CompositionLocalProvider(LocalAppLanguage provides selectedLanguage) {
                    Crossfade(
                        targetState = isSplashVisible,
                        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                        label = "splash_transition"
                    ) { showSplash ->
                        if (showSplash) {
                            SplashScreen(
                                onAnimationFinish = { isSplashVisible = false },
                                language = selectedLanguage
                            )
                        } else {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                containerColor = MinimalBackground,
                                contentWindowInsets = WindowInsets.safeDrawing,
                                topBar = {
                                    Surface(
                                        color = MinimalBackground,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, MinimalBorder.copy(alpha = 0.5f))
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .statusBarsPadding()
                                                .padding(horizontal = 16.dp, vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(14.dp))
                                                    .clickable { showProfileDialog = true }
                                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                                                    .testTag("btn_user_profile_header")
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(42.dp)
                                                        .background(CleanGreenLime, CircleShape),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = dynamicInitials,
                                                        color = CleanGreenDark,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp
                                                    )
                                                }
                                                Column {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                    ) {
                                                        Text(
                                                            text = AppStrings.greeting(selectedLanguage),
                                                            fontSize = 11.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            color = MinimalTextMuted
                                                        )
                                                        Icon(
                                                            imageVector = Icons.Default.Edit,
                                                            contentDescription = AppStrings.editName(selectedLanguage),
                                                            tint = MinimalTextMuted,
                                                            modifier = Modifier.size(11.dp)
                                                        )
                                                    }
                                                    Text(
                                                        text = userProfile.name,
                                                        fontSize = 15.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = MinimalTextPrimary
                                                    )
                                                    Text(
                                                        text = if (userProfile.staffUnlocked) "Extension / Official" else "Farmer",
                                                        fontSize = 10.sp,
                                                        color = MinimalTextMuted
                                                    )
                                                }
                                            }

                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Surface(
                                                    color = Color.White,
                                                    shape = CircleShape,
                                                    border = androidx.compose.foundation.BorderStroke(1.dp, MinimalBorder),
                                                    modifier = Modifier.size(38.dp)
                                                ) {
                                                    IconButton(
                                                        onClick = { showLanguageDialog = true },
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .testTag("btn_language_selector")
                                                    ) {
                                                        Icon(
                                                            Icons.Default.Translate,
                                                            contentDescription = "Language: ${selectedLanguage.nativeName}",
                                                            tint = CleanGreenPrimary,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                },
                                bottomBar = {
                                    Surface(
                                        color = MinimalSurfaceVariant,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, MinimalBorder)
                                    ) {
                                        NavigationBar(
                                            containerColor = MinimalSurfaceVariant,
                                            tonalElevation = 0.dp
                                        ) {
                                            visibleDestinations.forEach { destination ->
                                                val isSelected = currentDestination == destination
                                                val localizedTitle = destination.getLocalizedTitle(selectedLanguage)
                                                NavigationBarItem(
                                                    selected = isSelected,
                                                    onClick = { currentDestination = destination },
                                                    icon = {
                                                        Icon(
                                                            imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                                                            contentDescription = localizedTitle,
                                                            modifier = Modifier.size(22.dp)
                                                        )
                                                    },
                                                    label = {
                                                        Text(
                                                            text = localizedTitle,
                                                            fontSize = 10.sp,
                                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                                        )
                                                    },
                                                    colors = NavigationBarItemDefaults.colors(
                                                        selectedIconColor = CleanGreenPrimary,
                                                        selectedTextColor = CleanGreenPrimary,
                                                        unselectedIconColor = MinimalTextSecondary.copy(alpha = 0.7f),
                                                        unselectedTextColor = MinimalTextSecondary.copy(alpha = 0.7f),
                                                        indicatorColor = CleanGreenLime
                                                    ),
                                                    modifier = Modifier.testTag(destination.testTag)
                                                )
                                            }
                                        }
                                    }
                                }
                            ) { innerPadding ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                ) {
                                    Crossfade(
                                        targetState = currentDestination,
                                        label = "nav_crossfade"
                                    ) { destination ->
                                        when (destination) {
                                            AppNavigationDestination.DIAGNOSIS -> DiagnosisScreen(
                                                viewModel = viewModel,
                                                onNavigateToHotspots = { currentDestination = AppNavigationDestination.HOTSPOTS },
                                                onNavigateToIpm = { currentDestination = AppNavigationDestination.IPM_ADVISOR }
                                            )
                                            AppNavigationDestination.IPM_ADVISOR -> IPMAdvisorScreen(
                                                viewModel = viewModel,
                                                onNavigateToDiagnosis = { currentDestination = AppNavigationDestination.DIAGNOSIS }
                                            )
                                            AppNavigationDestination.HOTSPOTS -> HotspotMapScreen(
                                                viewModel = viewModel
                                            )
                                            AppNavigationDestination.FORECASTING -> RiskForecastScreen(
                                                viewModel = viewModel,
                                                onNavigateToDiagnosis = { currentDestination = AppNavigationDestination.DIAGNOSIS }
                                            )
                                            AppNavigationDestination.EXTENSION -> ExtensionPortalScreen(
                                                viewModel = viewModel
                                            )
                                            AppNavigationDestination.DASHBOARD -> OfficialDashboardScreen(
                                                viewModel = viewModel
                                            )
                                        }
                                    }
                                }
                            }

                            if (showLanguageDialog) {
                                LanguageSelectorDialog(
                                    currentLanguage = selectedLanguage,
                                    onLanguageSelected = { lang -> viewModel.setLanguage(lang) },
                                    onDismiss = { showLanguageDialog = false }
                                )
                            }

                            if (showProfileDialog) {
                                EditProfileDialog(
                                    currentName = userProfile.name,
                                    staffUnlocked = userProfile.staffUnlocked,
                                    onDismiss = { showProfileDialog = false },
                                    onSave = { newName ->
                                        viewModel.updateUserName(newName)
                                        showProfileDialog = false
                                    },
                                    onUnlockStaff = { pin -> viewModel.unlockStaff(pin) },
                                    onLockStaff = {
                                        viewModel.lockStaff()
                                        if (currentDestination == AppNavigationDestination.EXTENSION ||
                                            currentDestination == AppNavigationDestination.DASHBOARD
                                        ) {
                                            currentDestination = AppNavigationDestination.DIAGNOSIS
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
