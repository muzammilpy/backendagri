package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CropReport
import com.example.model.HotspotZone
import com.example.model.ThreatSeverity
import com.example.ui.components.GeospatialHotspotVisualizer
import com.example.ui.components.SeverityBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.CropHealthViewModel
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage

@Composable
fun HotspotMapScreen(
    viewModel: CropHealthViewModel,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    val allReports by viewModel.allReports.collectAsState()
    val allHotspots by viewModel.allHotspots.collectAsState()
    val cropFilter by viewModel.cropFilter.collectAsState()
    val severityFilter by viewModel.severityFilter.collectAsState()
    val allAlerts by viewModel.allAlerts.collectAsState()

    val cropFilterOptions = listOf("All", "Tomato & Potato", "Rice", "Maize", "Cotton", "Chilli")

    val filteredHotspots = remember(allHotspots, cropFilter, severityFilter) {
        allHotspots.filter { zone ->
            val matchCrop = (cropFilter == "All" || zone.crop.contains(cropFilter.take(4), ignoreCase = true))
            val matchSeverity = (severityFilter == null || zone.severity == severityFilter)
            matchCrop && matchSeverity
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Community Alert Banner
        item {
            allAlerts.firstOrNull()?.let { alert ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CriticalRedContainer),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().testTag("hotspot_alert_card")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(CriticalRed, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Emergency, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(AppStrings.regionalOutbreakAlert(lang), color = CriticalRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(alert.date, color = Color.Gray, fontSize = 10.sp)
                            }
                            Text(AppStrings.localizeAlertTitle(alert.title, lang), fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepJungle)
                            Text(AppStrings.localizeActionRequired(alert.actionRequired, lang), fontSize = 11.sp, color = DeepJungle, maxLines = 2)
                        }
                    }
                }
            }
        }

        // Surveillance Crop & Severity Filter Chips Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = AppStrings.surveillanceFilters(lang),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(cropFilterOptions) { cropOption ->
                        val isSelected = (cropFilter == cropOption)
                        val localizedCropLabel = if (cropOption == "All") AppStrings.allFilter(lang) else AppStrings.localizeCropName(cropOption, lang)
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setCropFilter(cropOption) },
                            label = { Text(localizedCropLabel, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ForestGreenPrimary,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.minimumInteractiveComponentSize().testTag("filter_chip_crop_${cropOption.take(4).lowercase()}")
                        )
                    }
                }

                // Severity Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = (severityFilter == null),
                            onClick = { viewModel.setSeverityFilter(null) },
                            label = { Text(AppStrings.allSeverities(lang), fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.minimumInteractiveComponentSize().testTag("filter_severity_all")
                        )
                    }
                    items(ThreatSeverity.values()) { severity ->
                        val isSelected = (severityFilter == severity)
                        val chipColor = when (severity) {
                            ThreatSeverity.CRITICAL -> CriticalRed
                            ThreatSeverity.HIGH -> WarningOrange
                            ThreatSeverity.MODERATE -> EarthAmber
                            ThreatSeverity.LOW -> LeafGreen
                        }
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) viewModel.setSeverityFilter(null)
                                else viewModel.setSeverityFilter(severity)
                            },
                            label = { Text(AppStrings.severityLabel(severity, lang), fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = chipColor,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.minimumInteractiveComponentSize().testTag("filter_severity_${severity.name.lowercase()}")
                        )
                    }
                }
            }
        }

        // Geospatial Hotspot Visualization Component (Map View & Sector Grid View with Diagnosis Data)
        item {
            GeospatialHotspotVisualizer(
                reports = allReports,
                hotspots = allHotspots,
                cropFilter = cropFilter,
                severityFilter = severityFilter,
                onReportSelected = { report ->
                    viewModel.setActiveReport(report)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Hotspot Zones List Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.officialEpidemicHotspots(filteredHotspots.size, lang),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary
                )
                Text(
                    text = AppStrings.radiusRange(lang),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        // Hotspot Cards
        items(filteredHotspots) { zone ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hotspot_zone_${zone.id}")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = zone.zoneName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = AppStrings.cropClusterRadius(AppStrings.localizeCropName(zone.crop, lang), zone.radiusKm, lang),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        SeverityBadge(severity = zone.severity)
                    }

                    // Threat description
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Coronavirus,
                                contentDescription = null,
                                tint = if (zone.severity == ThreatSeverity.CRITICAL) CriticalRed else EarthAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = zone.threatName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = AppStrings.confirmedFarmCases(zone.confirmedReportsCount, lang),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }

                    // Advisory Notice
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = ForestGreenPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.localizeAlertMessage(zone.advisoryNotice, lang),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

