package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.SeverityBadge
import com.example.ui.components.WeatherRiskAlertComponent
import com.example.ui.theme.*
import com.example.ui.viewmodel.CropHealthViewModel
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RiskForecastScreen(
    viewModel: CropHealthViewModel,
    onNavigateToDiagnosis: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    val forecasts by viewModel.weatherForecasts.collectAsState()
    val isWeatherSyncing by viewModel.isWeatherSyncing.collectAsState()
    val lastWeatherSyncTimestamp by viewModel.lastWeatherSyncTimestamp.collectAsState()
    val allTraps by viewModel.allTraps.collectAsState()
    val recentSensors by viewModel.recentSensors.collectAsState()
    val currentMicroclimate by viewModel.currentMicroclimate.collectAsState()
    val activeWeatherAlerts by viewModel.activeWeatherRiskAlerts.collectAsState()
    val isWeatherLoading by viewModel.isWeatherLoading.collectAsState()
    val availableStations = viewModel.availableWeatherStations

    var showAddTrapDialog by remember { mutableStateOf(false) }

    val formattedSyncTime = remember(lastWeatherSyncTimestamp) {
        val sdf = SimpleDateFormat("h:mm a, MMM d", Locale.getDefault())
        sdf.format(Date(lastWeatherSyncTimestamp))
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        // Real-Time Microclimate Weather Risk Alert Component
        item {
            WeatherRiskAlertComponent(
                weatherData = currentMicroclimate,
                alerts = activeWeatherAlerts,
                availableStations = availableStations,
                isLoading = isWeatherLoading || isWeatherSyncing,
                onSelectStation = { stationId -> viewModel.selectWeatherStation(stationId) },
                onRefresh = {
                    viewModel.refreshWeatherData()
                    viewModel.refreshWeatherForecasts()
                },
                onSimulateConditions = { temp, humidity, leafWetness, rain ->
                    viewModel.simulateWeatherConditions(temp, humidity, leafWetness, rain)
                },
                onApplyAlertToDiagnosis = { alert ->
                    viewModel.applyWeatherAlertToDiagnosis(alert)
                    onNavigateToDiagnosis()
                }
            )
        }

        // Live Open-Meteo Weather Status & Pull-to-Refresh Banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("live_weather_sync_banner"),
                shape = RoundedCornerShape(16.dp),
                color = MinimalSurfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MintSoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SatelliteAlt,
                                contentDescription = "Live Weather Feed",
                                tint = ForestGreenPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "LIVE OPEN-METEO TELEMETRY",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreenPrimary,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(SafeGreen, CircleShape)
                                )
                            }
                            Text(
                                text = "Synced: $formattedSyncTime",
                                fontSize = 11.sp,
                                color = MinimalTextSecondary
                            )
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.refreshWeatherForecasts()
                            viewModel.refreshWeatherData()
                        },
                        enabled = !isWeatherSyncing,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForestGreenPrimary,
                            disabledContainerColor = MinimalSurfaceVariant
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("btn_refresh_live_forecast")
                    ) {
                        if (isWeatherSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(AppStrings.syncing(lang), fontSize = 11.sp)
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = AppStrings.refresh(lang),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AppStrings.refresh(lang), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Forecast Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ForestGreenPrimary),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppStrings.forecastHeader(lang),
                            color = MintSoft,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        val highFungalCount = forecasts.count { it.fungalSporeRisk == ThreatSeverity.CRITICAL || it.fungalSporeRisk == ThreatSeverity.HIGH }
                        Surface(
                            color = if (highFungalCount > 0) CriticalRed else SafeGreen,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (highFungalCount > 0) "$highFungalCount Days High Risk" else "Optimal Microclimate",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = AppStrings.forecastSubtitle(lang),
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Calculated in real-time from relative humidity (>80%), precipitation, and diurnal temperatures to predict Late Blight, Mildew, and insect vector spikes.",
                        color = MintSoft,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Dynamic Day Forecast Cards
        items(forecasts) { forecast ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MinimalSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("forecast_${forecast.dayLabel.lowercase().replace(" ", "_")}")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Header Row: Day Label, Dynamic Date & Temperature
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(getWeatherConditionBg(forecast.conditionDescription)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getWeatherConditionIcon(forecast.conditionDescription),
                                    contentDescription = forecast.conditionDescription,
                                    tint = getWeatherConditionTint(forecast.conditionDescription),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "${forecast.dayLabel} • ${forecast.dateText}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MinimalTextPrimary
                                )
                                Text(
                                    text = forecast.conditionDescription,
                                    fontSize = 11.sp,
                                    color = MinimalTextSecondary
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${forecast.tempMax}° / ${forecast.tempMin}°C",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = ForestGreenPrimary
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = InfoBlue,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = " ${forecast.humidityPercent}%  ",
                                    fontSize = 11.sp,
                                    color = MinimalTextSecondary
                                )
                                Icon(
                                    imageVector = Icons.Default.Thunderstorm,
                                    contentDescription = null,
                                    tint = SoftWarningText,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = " ${forecast.rainProbabilityPercent}%",
                                    fontSize = 11.sp,
                                    color = MinimalTextSecondary
                                )
                            }
                        }
                    }

                    // Risk Models Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val fungalSeverity = forecast.fungalSporeRisk
                        Surface(
                            color = when (fungalSeverity) {
                                ThreatSeverity.CRITICAL -> SoftCriticalCard
                                ThreatSeverity.HIGH -> SoftWarningCard
                                ThreatSeverity.MODERATE -> SoftWarningCard.copy(alpha = 0.4f)
                                ThreatSeverity.LOW -> SoftGreenCard
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                when (fungalSeverity) {
                                    ThreatSeverity.CRITICAL -> CriticalRed.copy(alpha = 0.3f)
                                    ThreatSeverity.HIGH -> SoftWarningText.copy(alpha = 0.3f)
                                    else -> MinimalBorder
                                }
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = when (fungalSeverity) {
                                        ThreatSeverity.CRITICAL -> CriticalRed
                                        ThreatSeverity.HIGH -> SoftWarningText
                                        else -> InfoBlue
                                    },
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(AppStrings.fungalSporeRisk(lang), fontSize = 9.sp, color = MinimalTextMuted)
                                    Text(
                                        text = AppStrings.severity(fungalSeverity, lang),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        softWrap = false,
                                        color = when (fungalSeverity) {
                                            ThreatSeverity.CRITICAL -> CriticalRed
                                            ThreatSeverity.HIGH -> SoftWarningText
                                            else -> MinimalTextPrimary
                                        }
                                    )
                                }
                            }
                        }

                        val pestSeverity = forecast.pestVectorRisk
                        Surface(
                            color = when (pestSeverity) {
                                ThreatSeverity.CRITICAL -> SoftCriticalCard
                                ThreatSeverity.HIGH -> SoftWarningCard
                                ThreatSeverity.MODERATE -> SoftWarningCard.copy(alpha = 0.4f)
                                ThreatSeverity.LOW -> SoftGreenCard
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                when (pestSeverity) {
                                    ThreatSeverity.CRITICAL -> CriticalRed.copy(alpha = 0.3f)
                                    ThreatSeverity.HIGH -> SoftWarningText.copy(alpha = 0.3f)
                                    else -> MinimalBorder
                                }
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.BugReport,
                                    contentDescription = null,
                                    tint = when (pestSeverity) {
                                        ThreatSeverity.CRITICAL -> CriticalRed
                                        ThreatSeverity.HIGH -> SoftWarningText
                                        else -> EarthAmber
                                    },
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(AppStrings.pestVectorRisk(lang), fontSize = 9.sp, color = MinimalTextMuted)
                                    Text(
                                        text = AppStrings.severity(pestSeverity, lang),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        softWrap = false,
                                        color = when (pestSeverity) {
                                            ThreatSeverity.CRITICAL -> CriticalRed
                                            ThreatSeverity.HIGH -> SoftWarningText
                                            else -> MinimalTextPrimary
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Actionable Advisory
                    Surface(
                        color = SoftGreenCard,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SoftGreenBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.TipsAndUpdates,
                                contentDescription = null,
                                tint = ForestGreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.localizeAdvisory(forecast.actionableAdvisory, forecast.humidityPercent, lang),
                                fontSize = 11.sp,
                                color = MinimalTextPrimary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }

        // Pest-Trap Monitor & Economic Threshold Level (ETL)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PEST-TRAP LOGS & THRESHOLD ALARMS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary
                )
                Button(
                    onClick = { showAddTrapDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("btn_add_trap_log")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Log Trap Catch", fontSize = 11.sp)
                }
            }
        }

        // Trap Logs List
        items(allTraps) { trap ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (trap.thresholdExceeded) SoftCriticalCard else MinimalSurface
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (trap.thresholdExceeded) CriticalRed.copy(alpha = 0.3f) else MinimalBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("trap_item_${trap.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = trap.targetPest,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MinimalTextPrimary
                            )
                            Text(
                                text = "${trap.trapType.label} • ${trap.locationName}",
                                fontSize = 11.sp,
                                color = MinimalTextSecondary
                            )
                        }
                        Surface(
                            color = if (trap.thresholdExceeded) CriticalRed else SafeGreen,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (trap.thresholdExceeded) "EXCEEDS ETL (${trap.count}/${trap.economicThresholdLimit})" else "SAFE (${trap.count}/${trap.economicThresholdLimit})",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = trap.notes,
                        fontSize = 12.sp,
                        color = MinimalTextSecondary
                    )
                }
            }
        }

        // Field Sensor Telemetry Hub
        item {
            Text(
                text = "IOT CANOPY & SOIL SENSORS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary
            )
        }

        items(recentSensors) { sensor ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MinimalSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MinimalBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = sensor.fieldName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MinimalTextPrimary
                        )
                        if (sensor.isAnomaly) {
                            Surface(color = SoftWarningCard, shape = RoundedCornerShape(6.dp)) {
                                Text(
                                    text = "Prolonged Wetness Anomaly",
                                    color = SoftWarningText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(AppStrings.soilMoisture(lang), fontSize = 10.sp, color = MinimalTextMuted)
                            Text("${sensor.soilMoisturePercent.toInt()}%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MinimalTextPrimary)
                        }
                        Column {
                            Text(AppStrings.canopyTemp(lang), fontSize = 10.sp, color = MinimalTextMuted)
                            Text("${sensor.canopyTempC}°C", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MinimalTextPrimary)
                        }
                        Column {
                            Text(AppStrings.humidity(lang), fontSize = 10.sp, color = MinimalTextMuted)
                            Text("${sensor.relativeHumidityPercent.toInt()}%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MinimalTextPrimary)
                        }
                        Column {
                            Text(AppStrings.leafWetness(lang), fontSize = 10.sp, color = MinimalTextMuted)
                            Text(
                                "${sensor.leafWetnessHours} hrs",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (sensor.leafWetnessHours > 8) CriticalRed else SafeGreen
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Trap Log Dialog
    if (showAddTrapDialog) {
        var pestName by remember { mutableStateOf("Whitefly Vector") }
        var cropField by remember { mutableStateOf("Vegetable Polyhouse") }
        var trapCountText by remember { mutableStateOf("25") }
        var thresholdText by remember { mutableStateOf("15") }
        var locationText by remember { mutableStateOf("Plot 4 North") }
        var notesText by remember { mutableStateOf("High sticky card concentration near irrigation inlet.") }

        AlertDialog(
            onDismissRequest = { showAddTrapDialog = false },
            title = { Text("Log Pest-Trap Catch", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = pestName,
                        onValueChange = { pestName = it },
                        label = { Text("Target Pest") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = trapCountText,
                        onValueChange = { trapCountText = it },
                        label = { Text("Moths/Insects Counted") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = thresholdText,
                        onValueChange = { thresholdText = it },
                        label = { Text("Economic Threshold Limit (ETL)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = locationText,
                        onValueChange = { locationText = it },
                        label = { Text("Trap Location / Plot") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val count = trapCountText.toIntOrNull() ?: 0
                        val thresh = thresholdText.toIntOrNull() ?: 10
                        viewModel.addPestTrapLog(
                            TrapType.PHEROMONE,
                            pestName,
                            cropField,
                            count,
                            thresh,
                            notesText,
                            locationText
                        )
                        showAddTrapDialog = false
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(AppStrings.done(lang))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAddTrapDialog = false },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(AppStrings.cancel(lang))
                }
            }
        )
    }
}

private fun getWeatherConditionIcon(condition: String): ImageVector {
    val lower = condition.lowercase()
    return when {
        lower.contains("thunder") -> Icons.Default.Thunderstorm
        lower.contains("rain") || lower.contains("shower") || lower.contains("drizzle") -> Icons.Default.Grain
        lower.contains("fog") || lower.contains("mist") -> Icons.Default.CloudQueue
        lower.contains("sun") || lower.contains("clear") -> Icons.Default.WbSunny
        lower.contains("cloud") || lower.contains("overcast") -> Icons.Default.Cloud
        else -> Icons.Default.WbCloudy
    }
}

private fun getWeatherConditionBg(condition: String): Color {
    val lower = condition.lowercase()
    return when {
        lower.contains("thunder") -> SoftCriticalCard
        lower.contains("rain") || lower.contains("shower") -> SoftWarningCard
        lower.contains("sun") || lower.contains("clear") -> MintSoft
        else -> MinimalSurfaceVariant
    }
}

private fun getWeatherConditionTint(condition: String): Color {
    val lower = condition.lowercase()
    return when {
        lower.contains("thunder") -> CriticalRed
        lower.contains("rain") || lower.contains("shower") -> SoftWarningText
        lower.contains("sun") || lower.contains("clear") -> ForestGreenPrimary
        else -> MinimalTextSecondary
    }
}
