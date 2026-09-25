package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OfficialAlert
import com.example.model.ThreatSeverity
import com.example.ui.components.SeverityBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.CropHealthViewModel
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage

@Composable
fun OfficialDashboardScreen(
    viewModel: CropHealthViewModel,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    val allReports by viewModel.allReports.collectAsState()
    val allHotspots by viewModel.allHotspots.collectAsState()
    val allAlerts by viewModel.allAlerts.collectAsState()

    var showBroadcastDialog by remember { mutableStateOf(false) }

    val criticalOutbreaks = remember(allHotspots) {
        allHotspots.count { it.severity == ThreatSeverity.CRITICAL }
    }
    val highOutbreaks = remember(allHotspots) {
        allHotspots.count { it.severity == ThreatSeverity.HIGH }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        // District Command Hero
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ForestGreenPrimary),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().testTag("official_dashboard_banner")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MintSoft.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = AppStrings.stateAndDistrictCommandCenter(lang),
                                color = MintSoft,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Button(
                            onClick = { showBroadcastDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = EarthAmber),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("btn_broadcast_alert")
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AppStrings.broadcastAdvisoryBtn(lang), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = AppStrings.agriculturalHealthSurveillanceSystem(lang),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AppStrings.commandCenterSubtitle(lang),
                        fontSize = 12.sp,
                        color = MintSoft
                    )
                }
            }
        }

        // High Level KPI Stat Cards (2x2 Grid)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // KPI 1: Active Hotspots
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(AppStrings.activeHotspotsKpi(lang), fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                Icon(Icons.Default.Radar, contentDescription = null, tint = CriticalRed, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("${allHotspots.size}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                            Text(AppStrings.criticalClustersKpi(criticalOutbreaks, lang), fontSize = 10.sp, color = CriticalRed, fontWeight = FontWeight.Bold)
                        }
                    }

                    // KPI 2: Field Diagnoses
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(AppStrings.totalDiagnosesKpi(lang), fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                Icon(Icons.Default.Spa, contentDescription = null, tint = SafeGreen, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("${allReports.size}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                            Text(AppStrings.ipmTargetedKpi(lang), fontSize = 10.sp, color = SafeGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // KPI 3: Chemical Residue Risk Reduction
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(AppStrings.residueReductionKpi(lang), fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                Icon(Icons.AutoMirrored.Filled.TrendingDown, contentDescription = null, tint = SafeGreen, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("-38.4%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = SafeGreen)
                            Text(AppStrings.overSprayAvoidedViaPhi(lang), fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                        }
                    }

                    // KPI 4: Early Detection Latency
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(AppStrings.detectionLatencyKpi(lang), fontSize = 11.sp, color = MaterialTheme.colorScheme.outline)
                                Icon(Icons.Default.Timer, contentDescription = null, tint = InfoBlue, modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("< 4 hrs", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = InfoBlue)
                            Text(AppStrings.vsTraditionalLab(lang), fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }
        }

        // Integrated Pest Management vs Chemical Spray Ratio
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CreamSurface),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppStrings.safeInputUsageHeader(lang),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EarthAmber
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = AppStrings.safeInputUsageDescription(lang),
                        fontSize = 12.sp,
                        color = DeepJungle,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).background(SafeGreen, CircleShape))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AppStrings.bioCulturalStat(lang), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SafeGreen)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).background(WarningOrange, CircleShape))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AppStrings.targetedChemicalStat(lang), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WarningOrange)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).background(CriticalRed, CircleShape))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AppStrings.blanketSprayStat(lang), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CriticalRed)
                        }
                    }
                }
            }
        }

        // Official Broadcast Advisories Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.activeDistrictBroadcastAdvisories(allAlerts.size, lang),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary
                )
            }
        }

        // Alerts List
        items(allAlerts) { alert ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().testTag("official_alert_${alert.id}")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppStrings.localizeAlertTitle(alert.title, lang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = AppStrings.zoneAndCrops(alert.broadcastZone, alert.targetCrops, lang),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        SeverityBadge(severity = alert.severity)
                    }

                    Text(
                        text = AppStrings.localizeAlertMessage(alert.message, lang),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.DoneAll, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.actionLabel(AppStrings.localizeActionRequired(alert.actionRequired, lang), lang),
                                fontSize = 11.sp,
                                color = ForestGreenPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(AppStrings.issuedByLabel(alert.issuedBy, lang), fontSize = 10.sp, color = Color.Gray)
                        Text(alert.date, fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        }
    }

    // Broadcast Advisory Modal Dialog
    if (showBroadcastDialog) {
        var alertTitle by remember { mutableStateOf("Urgent District Advisory: Downy Mildew & Blast Alert") }
        var broadcastZone by remember { mutableStateOf("All Southern & Delta Agricultural Blocks") }
        var targetCrops by remember { mutableStateOf("Grapes, Tomato, Paddy") }
        var advisoryMessage by remember { mutableStateOf("Elevated continuous canopy wetness (>10 hrs) and overcast sky forecast for the next 48 hours. Farmers are instructed to apply preventative bio-fungicides.") }
        var actionText by remember { mutableStateOf("Inspect underside of leaves; apply protective Trichoderma or Copper Hydroxide spray.") }
        var alertSeverity by remember { mutableStateOf(ThreatSeverity.HIGH) }

        AlertDialog(
            onDismissRequest = { showBroadcastDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Campaign, contentDescription = null, tint = EarthAmber)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(AppStrings.broadcastDistrictAdvisoryTitle(lang), fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = alertTitle,
                        onValueChange = { alertTitle = it },
                        label = { Text(AppStrings.advisoryTitleLabel(lang)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = broadcastZone,
                        onValueChange = { broadcastZone = it },
                        label = { Text(AppStrings.broadcastAgroZoneLabel(lang)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = targetCrops,
                        onValueChange = { targetCrops = it },
                        label = { Text(AppStrings.targetCropsLabel(lang)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = advisoryMessage,
                        onValueChange = { advisoryMessage = it },
                        label = { Text(AppStrings.advisoryMessageLabel(lang)) },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = actionText,
                        onValueChange = { actionText = it },
                        label = { Text(AppStrings.requiredActionForFarmersLabel(lang)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addOfficialAlert(
                            title = alertTitle,
                            zone = broadcastZone,
                            crops = targetCrops,
                            severity = alertSeverity,
                            message = advisoryMessage,
                            issuedBy = "District Agriculture Officer (Surveillance Wing)",
                            action = actionText
                        )
                        showBroadcastDialog = false
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(AppStrings.broadcastNowBtn(lang))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showBroadcastDialog = false },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(AppStrings.cancelBtn(lang))
                }
            }
        )
    }
}
