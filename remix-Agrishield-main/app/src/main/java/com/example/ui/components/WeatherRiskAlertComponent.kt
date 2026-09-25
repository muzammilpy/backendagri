package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.MicroclimateWeatherData
import com.example.model.ThreatSeverity
import com.example.model.WeatherRiskThresholdAlert
import com.example.model.WeatherThresholdCategory
import com.example.ui.theme.*
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage

/**
 * Weather Risk Alert Component that displays real-time agricultural microclimate telemetry
 * and evaluates disease & pest threshold alerts (e.g. humidity, leaf wetness, heatwave).
 */
@Composable
fun WeatherRiskAlertComponent(
    weatherData: MicroclimateWeatherData,
    alerts: List<WeatherRiskThresholdAlert>,
    availableStations: List<MicroclimateWeatherData>,
    isLoading: Boolean,
    onSelectStation: (String) -> Unit,
    onRefresh: () -> Unit,
    onSimulateConditions: (temp: Float, humidity: Float, leafWetness: Float, rain: Float) -> Unit,
    onApplyAlertToDiagnosis: ((WeatherRiskThresholdAlert) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    var showSimulator by remember { mutableStateOf(false) }
    var expandedAlertId by remember { mutableStateOf<String?>(null) }

    // Count high/critical threats
    val criticalCount = alerts.count { it.severity == ThreatSeverity.CRITICAL }
    val highCount = alerts.count { it.severity == ThreatSeverity.HIGH }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("weather_risk_alert_component"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MinimalSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MinimalBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Bar with Telemetry Station Title & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (criticalCount > 0) SoftCriticalCard else if (highCount > 0) SoftWarningCard else SoftGreenCard
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (criticalCount > 0) Icons.Default.Warning else Icons.Default.CloudQueue,
                            contentDescription = "Weather Alert",
                            tint = if (criticalCount > 0) CriticalRed else if (highCount > 0) SoftWarningText else SafeGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = AppStrings.microclimateRiskAlerts(lang),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (criticalCount > 0) CriticalRed else ForestGreenPrimary,
                                letterSpacing = 0.5.sp
                            )
                            if (criticalCount > 0) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = CriticalRed,
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.wrapContentWidth()
                                ) {
                                    Text(
                                        text = "$criticalCount CRITICAL",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        softWrap = false,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = weatherData.stationName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MinimalTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Header Action Buttons (Simulator Toggle & Refresh)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { showSimulator = !showSimulator },
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                if (showSimulator) ForestGreenPrimary else MinimalSurfaceVariant,
                                CircleShape
                            )
                            .testTag("btn_toggle_weather_simulator")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Threshold Simulator",
                            tint = if (showSimulator) Color.White else MinimalTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = onRefresh,
                        enabled = !isLoading,
                        modifier = Modifier
                            .size(36.dp)
                            .background(MinimalSurfaceVariant, CircleShape)
                            .testTag("btn_refresh_weather_alerts")
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = ForestGreenPrimary
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Weather",
                                tint = MinimalTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Quick Station Selector Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(availableStations) { station ->
                    val isSelected = station.stationId == weatherData.stationId
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) ForestGreenPrimary else MinimalSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) ForestGreenPrimary else MinimalBorder
                        ),
                        modifier = Modifier
                            .clickable { onSelectStation(station.stationId) }
                            .testTag("chip_station_${station.stationId}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = when {
                                    station.relativeHumidityPercent >= 88f -> Icons.Default.WaterDrop
                                    station.temperatureC >= 34f -> Icons.Default.WbSunny
                                    station.leafWetnessHours >= 8f -> Icons.Default.Grain
                                    else -> Icons.Default.Sensors
                                },
                                contentDescription = null,
                                tint = if (isSelected) MintSoft else MinimalTextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = station.stationName.substringBefore(" &").take(16),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MinimalTextPrimary
                            )
                        }
                    }
                }
            }

            // Real-Time Threshold Gauges Grid (Temp, Humidity, Leaf Wetness, Rain)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Relative Humidity Metric & Threshold Indicator
                WeatherMetricCard(
                    title = "Humidity",
                    value = "${weatherData.relativeHumidityPercent.toInt()}%",
                    subtext = if (weatherData.relativeHumidityPercent >= 80f) "⚠️ Exceeds >80%" else "Safe Level",
                    icon = Icons.Default.WaterDrop,
                    isAlert = weatherData.relativeHumidityPercent >= 80f,
                    alertColor = if (weatherData.relativeHumidityPercent >= 88f) CriticalRed else SoftWarningText,
                    badgeColor = if (weatherData.relativeHumidityPercent >= 88f) SoftCriticalCard else SoftWarningCard,
                    modifier = Modifier.weight(1f)
                )

                // Temperature Metric & Threshold Indicator
                WeatherMetricCard(
                    title = "Temperature",
                    value = "${weatherData.temperatureC}°C",
                    subtext = if (weatherData.temperatureC >= 33f) "⚠️ Pest Surge" else if (weatherData.temperatureC in 18f..26f && weatherData.relativeHumidityPercent >= 80f) "⚠️ Spore Window" else "Optimal",
                    icon = Icons.Default.Thermostat,
                    isAlert = weatherData.temperatureC >= 33f || (weatherData.temperatureC in 18f..26f && weatherData.relativeHumidityPercent >= 80f),
                    alertColor = if (weatherData.temperatureC >= 35f) CriticalRed else SoftWarningText,
                    badgeColor = if (weatherData.temperatureC >= 35f) SoftCriticalCard else SoftWarningCard,
                    modifier = Modifier.weight(1f)
                )

                // Leaf Wetness Duration Metric
                WeatherMetricCard(
                    title = "Leaf Wetness",
                    value = "${weatherData.leafWetnessHours}h",
                    subtext = if (weatherData.leafWetnessHours >= 6f) "⚠️ Blight Risk" else "Normal",
                    icon = Icons.Default.Opacity,
                    isAlert = weatherData.leafWetnessHours >= 6f,
                    alertColor = if (weatherData.leafWetnessHours >= 8f) CriticalRed else SoftWarningText,
                    badgeColor = if (weatherData.leafWetnessHours >= 8f) SoftCriticalCard else SoftWarningCard,
                    modifier = Modifier.weight(1f)
                )

                // Precipitation / Rain Metric
                WeatherMetricCard(
                    title = "Rainfall",
                    value = "${weatherData.rainfallMm}mm",
                    subtext = if (weatherData.rainfallMm >= 15f) "⚠️ Saturation" else "Dry",
                    icon = Icons.Default.Thunderstorm,
                    isAlert = weatherData.rainfallMm >= 15f,
                    alertColor = SoftWarningText,
                    badgeColor = SoftWarningCard,
                    modifier = Modifier.weight(1f)
                )
            }

            // Visual Humidity Threshold Progression Bar
            HumidityThresholdBar(
                currentHumidity = weatherData.relativeHumidityPercent,
                modifier = Modifier.fillMaxWidth()
            )

            // Interactive Threshold Simulator Drawer
            AnimatedVisibility(
                visible = showSimulator,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                ThresholdSimulatorPanel(
                    initialTemp = weatherData.temperatureC,
                    initialHumidity = weatherData.relativeHumidityPercent,
                    initialLeafWetness = weatherData.leafWetnessHours,
                    initialRain = weatherData.rainfallMm,
                    onApplySimulation = onSimulateConditions
                )
            }

            HorizontalDivider(color = MinimalBorder)

            // Active Weather Risk Alerts Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ACTIVE DISEASE & PEST THRESHOLD ALERTS (${alerts.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary
                )
                Text(
                    text = "Auto-evaluated by IPM Rules",
                    fontSize = 10.sp,
                    color = MinimalTextMuted
                )
            }

            // List of Evaluated Weather Risk Alerts
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                alerts.forEach { alert ->
                    val isExpanded = expandedAlertId == alert.alertId
                    WeatherRiskAlertCard(
                        alert = alert,
                        isExpanded = isExpanded,
                        onToggleExpand = {
                            expandedAlertId = if (isExpanded) null else alert.alertId
                        },
                        onApplyToDiagnosis = onApplyAlertToDiagnosis
                    )
                }
            }
        }
    }
}

/**
 * Individual Metric Card with threshold alert status
 */
@Composable
private fun WeatherMetricCard(
    title: String,
    value: String,
    subtext: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isAlert: Boolean,
    alertColor: Color,
    badgeColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (isAlert) badgeColor else MinimalSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isAlert) alertColor.copy(alpha = 0.4f) else MinimalBorder
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isAlert) alertColor else ForestGreenPrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isAlert) alertColor else MinimalTextPrimary
            )
            Text(
                text = title,
                fontSize = 10.sp,
                color = MinimalTextMuted
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtext,
                fontSize = 9.sp,
                fontWeight = if (isAlert) FontWeight.Bold else FontWeight.Normal,
                color = if (isAlert) alertColor else MinimalTextSecondary,
                maxLines = 1
            )
        }
    }
}

/**
 * Visual gradient progression bar showing Humidity thresholds (<70% Safe, 70-80% Caution, >80% Critical Spore Surge)
 */
@Composable
private fun HumidityThresholdBar(
    currentHumidity: Float,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MinimalSurfaceVariant, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Relative Humidity Threshold Range",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = MinimalTextSecondary
            )
            Text(
                text = if (currentHumidity >= 80f) "CRITICAL SPORE ZONE (>80%)" else "Within Safe Range",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (currentHumidity >= 80f) CriticalRed else SafeGreen
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        // Multi-segment progress track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            SafeGreen.copy(alpha = 0.7f),
                            WarningOrange.copy(alpha = 0.8f),
                            CriticalRed
                        )
                    )
                )
        )

        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0% (Dry)", fontSize = 8.sp, color = MinimalTextMuted)
            Text("70% (Caution)", fontSize = 8.sp, color = MinimalTextMuted)
            Text("80% (Fungal Spore Threshold)", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = CriticalRed)
            Text("100%", fontSize = 8.sp, color = MinimalTextMuted)
        }
    }
}

/**
 * Detailed Weather Risk Alert Card with IPM Advisory and Diagnosis Integration
 */
@Composable
fun WeatherRiskAlertCard(
    alert: WeatherRiskThresholdAlert,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onApplyToDiagnosis: ((WeatherRiskThresholdAlert) -> Unit)?,
    modifier: Modifier = Modifier
) {
    val cardLang = LocalAppLanguage.current
    val cardBackground = when (alert.severity) {
        ThreatSeverity.CRITICAL -> SoftCriticalCard
        ThreatSeverity.HIGH -> SoftWarningCard
        ThreatSeverity.MODERATE -> SoftWarningCard.copy(alpha = 0.5f)
        ThreatSeverity.LOW -> SoftGreenCard
    }

    val accentColor = when (alert.severity) {
        ThreatSeverity.CRITICAL -> CriticalRed
        ThreatSeverity.HIGH -> SoftWarningText
        ThreatSeverity.MODERATE -> SoftWarningText
        ThreatSeverity.LOW -> SafeGreen
    }

    val localizedTitle = when {
        alert.title.contains("Late Blight", ignoreCase = true) || alert.targetDisease.contains("Late Blight", ignoreCase = true) -> when (cardLang) {
            AppLanguage.ENGLISH -> "Critical Late Blight & Downy Mildew Spore Alert"
            AppLanguage.HINDI -> "गंभीर लेट ब्लाइट और डाउनी मिल्ड्यू बीजाणु चेतावनी"
            AppLanguage.MARATHI -> "गंभीर लेट ब्लाइट आणि डाऊनी मिल्ड्यू बीजाणू इशारा"
            AppLanguage.BENGALI -> "গুরুতর লেইট ব্লাইট এবং ডাউনি মিলডিউ সতর্কতা"
            AppLanguage.SPANISH -> "Alerta Crítica de Esporas de Tizón Tardío y Mildiu"
            AppLanguage.FRENCH -> "Alerte Critique aux Spores de Mildiou"
            AppLanguage.SWAHILI -> "Tahadhari Kubwa ya Spora za Late Blight na Downy Mildew"
        }
        alert.title.contains("Leaf Wetness", ignoreCase = true) -> when (cardLang) {
            AppLanguage.ENGLISH -> "Prolonged Leaf Wetness - Bacterial Blight Alert"
            AppLanguage.HINDI -> "लगातार पत्ती गीलापन - जीवाणु झुलसा चेतावनी"
            AppLanguage.MARATHI -> "दीर्घकाळ पानांचा ओलावा - जिवाणू करपा इशारा"
            AppLanguage.BENGALI -> "দীর্ঘস্থায়ী পাতার আর্দ্রতা - ব্যাকটেরিয়াল ব্লাইট সতর্কতা"
            AppLanguage.SPANISH -> "Humedad Foliar Prolongada - Alerta de Tizón Bacteriano"
            AppLanguage.FRENCH -> "Humectation Foliaire Prolongée - Alerte au Flétrissement Bactérien"
            AppLanguage.SWAHILI -> "Unyevu wa Majani wa Muda Mrefu - Tahadhari ya Bakteria"
        }
        alert.title.contains("Heatwave", ignoreCase = true) || alert.title.contains("Pest", ignoreCase = true) -> when (cardLang) {
            AppLanguage.ENGLISH -> "Heatwave Threshold - Sucking Pest Proliferation"
            AppLanguage.HINDI -> "हीटवेव सीमा - रसचूसक कीट वृद्धि चेतावनी"
            AppLanguage.MARATHI -> "उष्णतेची लाट - रसशोषक किडींचा प्रादुर्भाव"
            AppLanguage.BENGALI -> "তাপপ্রবাহ সীমা - শোষক পোকার বিস্তার"
            AppLanguage.SPANISH -> "Umbral de Ola de Calor - Proliferación de Plagas Chupadoras"
            AppLanguage.FRENCH -> "Seuil de Canicule - Prolifération des Ravageurs Piqueurs"
            AppLanguage.SWAHILI -> "Kiwango cha Wimbi la Joto - Ongezeko la Wadudu Wanyonyao"
        }
        else -> alert.title
    }

    val localizedIpmAdvisory = when {
        alert.targetDisease.contains("Late Blight", ignoreCase = true) || alert.ipmAdvisory.contains("Trichoderma", ignoreCase = true) || alert.alertId.contains("late_blight", ignoreCase = true) -> {
            AppStrings.criticalDiseaseAdvisoryAlert(90, cardLang)
        }
        else -> AppStrings.localizeAdvisory(alert.ipmAdvisory, 90, cardLang)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggleExpand() }
            .testTag("alert_card_${alert.alertId}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Top Row: Category Icon, Title, and Severity Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Icon(
                        imageVector = when (alert.category) {
                            WeatherThresholdCategory.HUMIDITY_HIGH -> Icons.Default.WaterDrop
                            WeatherThresholdCategory.LEAF_WETNESS -> Icons.Default.Opacity
                            WeatherThresholdCategory.HEAT_DROUGHT_VECTOR -> Icons.Default.WbSunny
                            WeatherThresholdCategory.DEW_POINT_INCUBATION -> Icons.Default.SevereCold
                            WeatherThresholdCategory.PRECIPITATION_WASH -> Icons.Default.Thunderstorm
                        },
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = localizedTitle,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MinimalTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))

                Surface(
                    color = accentColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "⚠️",
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = AppStrings.severity(alert.severity, cardLang),
                            color = accentColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            // Target Disease & Affected Crops
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎯 ${AppStrings.diseaseName(alert.targetDisease, cardLang)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ForestGreenPrimary
                )
                Text(
                    text = if (isExpanded) AppStrings.hideDetails(cardLang) else AppStrings.viewIpmAdvice(cardLang),
                    fontSize = 10.sp,
                    color = MinimalTextMuted,
                    fontWeight = FontWeight.Medium
                )
            }

            // Triggered Threshold Pill
            Surface(
                color = MinimalSurface.copy(alpha = 0.9f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = alert.triggeredThresholdText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MinimalTextPrimary
                    )
                }
            }

            // Affected Crops Tags
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(AppStrings.vulnerableCropsLabel(cardLang), fontSize = 10.sp, color = MinimalTextMuted)
                alert.affectedCrops.forEach { crop ->
                    Surface(
                        color = MinimalSurface,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = crop,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ForestGreenPrimary,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            // Expandable Epidemiology Mechanism & Actionable IPM Advisory
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Risk Mechanism
                    Surface(
                        color = MinimalSurface,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = AppStrings.pathogenMechanismLabel(cardLang),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MinimalTextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = alert.riskMechanism,
                                fontSize = 11.sp,
                                color = MinimalTextSecondary,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    // IPM Preventive Advisory
                    Surface(
                        color = SoftGreenCard,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SoftGreenBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = SafeGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.ipmPrecautionaryActionLabel(alert.urgency, cardLang),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SafeGreen
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = localizedIpmAdvisory,
                                fontSize = 11.sp,
                                color = MinimalTextPrimary,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    // Action Button: Apply Alert to Symptom Diagnosis
                    if (onApplyToDiagnosis != null) {
                        Button(
                            onClick = { onApplyToDiagnosis(alert) },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .minimumInteractiveComponentSize()
                                .testTag("btn_apply_alert_${alert.alertId}")
                        ) {
                            Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(AppStrings.applyWeatherRiskToAiForm(cardLang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Microclimate Threshold Simulator Panel
 * Allows farmers and agronomists to simulate custom weather conditions to see threshold triggers.
 */
@Composable
private fun ThresholdSimulatorPanel(
    initialTemp: Float,
    initialHumidity: Float,
    initialLeafWetness: Float,
    initialRain: Float,
    onApplySimulation: (temp: Float, humidity: Float, leafWetness: Float, rain: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var simTemp by remember(initialTemp) { mutableStateOf(initialTemp) }
    var simHumidity by remember(initialHumidity) { mutableStateOf(initialHumidity) }
    var simLeafWetness by remember(initialLeafWetness) { mutableStateOf(initialLeafWetness) }
    var simRain by remember(initialRain) { mutableStateOf(initialRain) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MinimalSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, MinimalBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Tune, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Microclimate Threshold Simulator",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MinimalTextPrimary
                    )
                }
                Text("Simulate Scenarios", fontSize = 10.sp, color = ForestGreenPrimary)
            }

            // Quick Scenario Presets
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PresetChip("Monsoon Spore Surge", Modifier.weight(1f)) {
                    simTemp = 23f; simHumidity = 92f; simLeafWetness = 8.5f; simRain = 22f
                    onApplySimulation(simTemp, simHumidity, simLeafWetness, simRain)
                }
                PresetChip("Heatwave Pest Spike", Modifier.weight(1f)) {
                    simTemp = 36.5f; simHumidity = 38f; simLeafWetness = 0.5f; simRain = 0f
                    onApplySimulation(simTemp, simHumidity, simLeafWetness, simRain)
                }
                PresetChip("Dew Fog Mildew", Modifier.weight(1f)) {
                    simTemp = 18f; simHumidity = 95f; simLeafWetness = 6.0f; simRain = 2f
                    onApplySimulation(simTemp, simHumidity, simLeafWetness, simRain)
                }
            }

            // Temperature Slider
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Canopy Temperature: ${simTemp.toInt()}°C", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Text(if (simTemp >= 33f) "Heat Threshold!" else "Normal", fontSize = 10.sp, color = if (simTemp >= 33f) CriticalRed else SafeGreen)
                }
                Slider(
                    value = simTemp,
                    onValueChange = {
                        simTemp = it
                        onApplySimulation(simTemp, simHumidity, simLeafWetness, simRain)
                    },
                    valueRange = 10f..45f,
                    colors = SliderDefaults.colors(thumbColor = ForestGreenPrimary, activeTrackColor = ForestGreenPrimary)
                )
            }

            // Humidity Slider
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Relative Humidity: ${simHumidity.toInt()}%", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Text(if (simHumidity >= 80f) "Spore Surge Threshold (>80%)" else "Safe", fontSize = 10.sp, color = if (simHumidity >= 80f) CriticalRed else SafeGreen)
                }
                Slider(
                    value = simHumidity,
                    onValueChange = {
                        simHumidity = it
                        onApplySimulation(simTemp, simHumidity, simLeafWetness, simRain)
                    },
                    valueRange = 20f..100f,
                    colors = SliderDefaults.colors(thumbColor = ForestGreenPrimary, activeTrackColor = ForestGreenPrimary)
                )
            }

            // Leaf Wetness Slider
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Leaf Wetness Duration: ${simLeafWetness.toInt()} hrs", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Text(if (simLeafWetness >= 6f) "Bacterial Ingress (>6h)" else "Safe", fontSize = 10.sp, color = if (simLeafWetness >= 6f) CriticalRed else SafeGreen)
                }
                Slider(
                    value = simLeafWetness,
                    onValueChange = {
                        simLeafWetness = it
                        onApplySimulation(simTemp, simHumidity, simLeafWetness, simRain)
                    },
                    valueRange = 0f..16f,
                    colors = SliderDefaults.colors(thumbColor = ForestGreenPrimary, activeTrackColor = ForestGreenPrimary)
                )
            }
        }
    }
}

@Composable
private fun PresetChip(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MinimalSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MinimalBorder),
        modifier = modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = ForestGreenPrimary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
