package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CropReport
import com.example.model.HotspotZone
import com.example.model.ThreatSeverity
import com.example.ui.theme.*
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

enum class HotspotViewMode(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    MAP_RADAR("Map Radar", Icons.Default.Radar),
    REGIONAL_GRID("Sector Grid", Icons.Default.GridView)
}

/**
 * Aggregated Outbreak Cluster computed from saved diagnosis reports and hotspot data.
 */
data class AggregatedOutbreakCluster(
    val id: String,
    val sectorName: String,
    val latitude: Double,
    val longitude: Double,
    val primaryDisease: String,
    val primaryCrop: String,
    val maxSeverity: ThreatSeverity,
    val reportCount: Int,
    val radiusKm: Float,
    val contributingReports: List<CropReport>,
    val advisorySummary: String,
    // Relative offset normalized for map canvas (-1f to 1f)
    val mapOffsetX: Float,
    val mapOffsetY: Float
)

/**
 * Computes aggregated regional outbreak clusters dynamically from saved diagnosis reports and hotspot zones.
 */
fun aggregateDiagnosisData(
    reports: List<CropReport>,
    hotspots: List<HotspotZone>
): List<AggregatedOutbreakCluster> {
    // 1. Group saved reports by village/sector or disease clusters
    val reportsByRegion = reports.groupBy { report ->
        val region = report.villageRegion.trim()
        if (region.isBlank()) "Central Agrarian Sector" else region
    }

    val clusters = mutableListOf<AggregatedOutbreakCluster>()

    // Map existing hotspots as base clusters or create from diagnosis data
    reportsByRegion.forEach { (regionName, regionReports) ->
        val primaryReport = regionReports.maxByOrNull { it.timestamp } ?: return@forEach
        val maxSeverity = regionReports.maxOfOrNull { it.severity } ?: ThreatSeverity.MODERATE
        val topDisease = regionReports.groupBy { it.diseaseName }
            .maxByOrNull { it.value.size }?.key ?: primaryReport.diseaseName
        val topCrop = regionReports.groupBy { it.cropName }
            .maxByOrNull { it.value.size }?.key ?: primaryReport.cropName

        // Compute normalized coordinate relative to central point (approx Lat: 18.530, Long: 73.858)
        val latCenter = 18.5300
        val lonCenter = 73.8580
        val avgLat = regionReports.map { it.latitude }.average().takeIf { !it.isNaN() } ?: 18.5250
        val avgLon = regionReports.map { it.longitude }.average().takeIf { !it.isNaN() } ?: 73.8500

        val normX = ((avgLon - lonCenter) * 40.0).toFloat().coerceIn(-0.85f, 0.85f)
        val normY = -((avgLat - latCenter) * 40.0).toFloat().coerceIn(-0.85f, 0.85f)

        clusters.add(
            AggregatedOutbreakCluster(
                id = "cluster_${regionName.hashCode()}",
                sectorName = regionName,
                latitude = avgLat,
                longitude = avgLon,
                primaryDisease = topDisease,
                primaryCrop = topCrop,
                maxSeverity = maxSeverity,
                reportCount = regionReports.size,
                radiusKm = (1.5f + (regionReports.size * 0.5f)).coerceAtMost(5.0f),
                contributingReports = regionReports,
                advisorySummary = when (maxSeverity) {
                    ThreatSeverity.CRITICAL -> "Active outbreak alert: immediate containment, fungicide rotation, and quarantine barrier advised."
                    ThreatSeverity.HIGH -> "High infection density: deploy pheromone traps, sticky cards, and early bio-fungicide sprays."
                    ThreatSeverity.MODERATE -> "Moderate risk detected: monitor leaf undersides and limit overhead moisture."
                    ThreatSeverity.LOW -> "Low prevalence: routine scouting and sanitation recommended."
                },
                mapOffsetX = normX,
                mapOffsetY = normY
            )
        )
    }

    // Also blend official Hotspot zones if no direct diagnosis was registered in that zone
    hotspots.forEach { zone ->
        val alreadyExists = clusters.any { it.sectorName.contains(zone.zoneName.take(6), ignoreCase = true) }
        if (!alreadyExists) {
            val normX = ((zone.longitude - 73.8580) * 40.0).toFloat().coerceIn(-0.85f, 0.85f)
            val normY = -((zone.latitude - 18.5300) * 40.0).toFloat().coerceIn(-0.85f, 0.85f)

            clusters.add(
                AggregatedOutbreakCluster(
                    id = "official_zone_${zone.id}",
                    sectorName = zone.zoneName,
                    latitude = zone.latitude,
                    longitude = zone.longitude,
                    primaryDisease = zone.threatName,
                    primaryCrop = zone.crop,
                    maxSeverity = zone.severity,
                    reportCount = zone.confirmedReportsCount,
                    radiusKm = zone.radiusKm,
                    contributingReports = emptyList(),
                    advisorySummary = zone.advisoryNotice,
                    mapOffsetX = if (normX == 0f) 0.35f else normX,
                    mapOffsetY = if (normY == 0f) -0.35f else normY
                )
            )
        }
    }

    return clusters
}

/**
 * Comprehensive Geospatial Hotspot Visualization Component.
 * Supports interactive Map/Radar canvas and Regional Grid Matrix modes,
 * with real-time aggregation from saved diagnosis records.
 */
@Composable
fun GeospatialHotspotVisualizer(
    reports: List<CropReport>,
    hotspots: List<HotspotZone>,
    cropFilter: String = "All",
    severityFilter: ThreatSeverity? = null,
    onReportSelected: ((CropReport) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    var viewMode by remember { mutableStateOf(HotspotViewMode.MAP_RADAR) }
    var selectedClusterId by remember { mutableStateOf<String?>(null) }
    var showHeatmapHalos by remember { mutableStateOf(true) }

    // Aggregate saved diagnosis records into regional clusters
    val allClusters = remember(reports, hotspots) {
        aggregateDiagnosisData(reports, hotspots)
    }

    // Filter clusters based on active crop and severity filters
    val filteredClusters = remember(allClusters, cropFilter, severityFilter) {
        allClusters.filter { cluster ->
            val matchCrop = (cropFilter == "All" || cluster.primaryCrop.contains(cropFilter.take(4), ignoreCase = true) ||
                    cluster.contributingReports.any { it.cropName.contains(cropFilter.take(4), ignoreCase = true) })
            val matchSeverity = (severityFilter == null || cluster.maxSeverity == severityFilter)
            matchCrop && matchSeverity
        }
    }

    // Auto-select first cluster if none selected
    LaunchedEffect(filteredClusters) {
        if (selectedClusterId == null && filteredClusters.isNotEmpty()) {
            selectedClusterId = filteredClusters.first().id
        } else if (filteredClusters.none { it.id == selectedClusterId }) {
            selectedClusterId = filteredClusters.firstOrNull()?.id
        }
    }

    val selectedCluster = remember(filteredClusters, selectedClusterId) {
        filteredClusters.find { it.id == selectedClusterId }
    }

    val totalDiagnosedCount = remember(reports) { reports.size }
    val criticalClustersCount = remember(filteredClusters) {
        filteredClusters.count { it.maxSeverity == ThreatSeverity.CRITICAL }
    }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("geospatial_hotspot_visualizer")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header with Visualizer Mode Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = AppStrings.geospatialSurveillanceHeader(lang),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                    Text(
                        text = AppStrings.aggregatedFromRecords(reports.size, lang),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Map vs Grid Mode Selector
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.testTag("hotspot_mode_toggle_group")
                ) {
                    Row(modifier = Modifier.padding(3.dp)) {
                        HotspotViewMode.values().forEach { mode ->
                            val isSelected = (viewMode == mode)
                            val modeLabel = when (mode) {
                                HotspotViewMode.MAP_RADAR -> AppStrings.mapRadarMode(lang)
                                HotspotViewMode.REGIONAL_GRID -> AppStrings.sectorGridMode(lang)
                            }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) ForestGreenPrimary else Color.Transparent,
                                modifier = Modifier
                                    .clickable { viewMode = mode }
                                    .testTag("btn_mode_${mode.name.lowercase()}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = mode.icon,
                                        contentDescription = modeLabel,
                                        tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = modeLabel,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Overview Metrics Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetricItem(label = AppStrings.activeClustersLabel(lang), value = "${filteredClusters.size}", tint = ForestGreenPrimary)
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)))
                MetricItem(label = AppStrings.severeOutbreaksLabel(lang), value = "$criticalClustersCount", tint = if (criticalClustersCount > 0) CriticalRed else EarthAmber)
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)))
                MetricItem(label = AppStrings.diagnosisCasesLabel(lang), value = "$totalDiagnosedCount", tint = LeafGreen)
                Box(modifier = Modifier.width(1.dp).height(24.dp).background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)))
                MetricItem(label = AppStrings.maxRadiusLabel(lang), value = "5.0 km", tint = MaterialTheme.colorScheme.onSurface)
            }

            // Main Visualization Container (Map View vs Grid View)
            AnimatedContent(
                targetState = viewMode,
                label = "hotspot_view_transition"
            ) { currentMode ->
                when (currentMode) {
                    HotspotViewMode.MAP_RADAR -> {
                        InteractiveMapRadarCanvas(
                            clusters = filteredClusters,
                            selectedClusterId = selectedClusterId,
                            showHeatmapHalos = showHeatmapHalos,
                            onSelectCluster = { selectedClusterId = it }
                        )
                    }
                    HotspotViewMode.REGIONAL_GRID -> {
                        RegionalSectorGridMatrix(
                            clusters = filteredClusters,
                            selectedClusterId = selectedClusterId,
                            onSelectCluster = { selectedClusterId = it }
                        )
                    }
                }
            }

            // Selected Cluster Interactive Inspection Detail Card
            selectedCluster?.let { cluster ->
                ClusterDetailInspectionCard(
                    cluster = cluster,
                    onReportClick = { report -> onReportSelected?.invoke(report) }
                )
            }
        }
    }
}

@Composable
private fun MetricItem(label: String, value: String, tint: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = tint)
        Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
    }
}

/**
 * Interactive Radar & Geospatial Coordinate Canvas.
 * Renders radar rings, animated sweep line, and interactive cluster nodes based on saved diagnosis GPS.
 */
@Composable
fun InteractiveMapRadarCanvas(
    clusters: List<AggregatedOutbreakCluster>,
    selectedClusterId: String?,
    showHeatmapHalos: Boolean,
    onSelectCluster: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    val infiniteTransition = rememberInfiniteTransition(label = "radar_anim")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep_angle"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF132A1F), Color(0xFF081C15))
                )
            )
            .testTag("interactive_map_radar_canvas")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxRadius = (size.minDimension / 2f) - 24.dp.toPx()

            // Concentric Range Rings (1km, 2.5km, 4km, 5km)
            val rings = 4
            for (i in 1..rings) {
                val r = (maxRadius / rings) * i
                drawCircle(
                    color = MintLight.copy(alpha = 0.22f),
                    radius = r,
                    center = center,
                    style = Stroke(width = 1.2.dp.toPx())
                )
            }

            // Coordinate Crosshairs & Compass Grid
            drawLine(
                color = MintLight.copy(alpha = 0.2f),
                start = Offset(center.x - maxRadius, center.y),
                end = Offset(center.x + maxRadius, center.y),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = MintLight.copy(alpha = 0.2f),
                start = Offset(center.x, center.y - maxRadius),
                end = Offset(center.x, center.y + maxRadius),
                strokeWidth = 1.dp.toPx()
            )

            // Radar Sweep Beam
            val rad = Math.toRadians(angle.toDouble())
            val sweepEnd = Offset(
                x = center.x + (maxRadius * cos(rad)).toFloat(),
                y = center.y + (maxRadius * sin(rad)).toFloat()
            )
            drawLine(
                brush = Brush.radialGradient(
                    colors = listOf(MintLight.copy(alpha = 0.85f), Color.Transparent),
                    center = center,
                    radius = maxRadius
                ),
                start = center,
                end = sweepEnd,
                strokeWidth = 3.dp.toPx()
            )

            // Draw Outbreak Cluster Nodes & Halos
            clusters.forEach { cluster ->
                val clusterCenter = Offset(
                    x = center.x + (cluster.mapOffsetX * maxRadius),
                    y = center.y + (cluster.mapOffsetY * maxRadius)
                )

                val color = when (cluster.maxSeverity) {
                    ThreatSeverity.CRITICAL -> CriticalRed
                    ThreatSeverity.HIGH -> WarningOrange
                    ThreatSeverity.MODERATE -> EarthAmber
                    ThreatSeverity.LOW -> LeafGreen
                }

                val isSelected = (cluster.id == selectedClusterId)

                // Heatmap Halo
                if (showHeatmapHalos) {
                    val haloRadius = (16.dp.toPx() + (cluster.reportCount * 4.dp.toPx())) * (if (cluster.maxSeverity == ThreatSeverity.CRITICAL) pulse else 1f)
                    drawCircle(
                        color = color.copy(alpha = if (isSelected) 0.45f else 0.25f),
                        radius = haloRadius,
                        center = clusterCenter
                    )
                }

                // Core Pin Marker
                drawCircle(
                    color = color,
                    radius = if (isSelected) 8.dp.toPx() else 6.dp.toPx(),
                    center = clusterCenter
                )
                drawCircle(
                    color = Color.White,
                    radius = if (isSelected) 3.5.dp.toPx() else 2.5.dp.toPx(),
                    center = clusterCenter
                )

                // Outer selection ring
                if (isSelected) {
                    drawCircle(
                        color = Color.White,
                        radius = 12.dp.toPx(),
                        center = clusterCenter,
                        style = Stroke(width = 1.8.dp.toPx())
                    )
                }
            }

            // Central Farm Base Station
            drawCircle(
                color = MintLight,
                radius = 5.dp.toPx(),
                center = center
            )
        }

        // Overlay Interactive Clickable Nodes for easy touch targets
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val centerXPx = maxWidth.value / 2f
            val centerYPx = maxHeight.value / 2f
            val maxRadiusDp = (minOf(maxWidth.value, maxHeight.value) / 2f) - 24f

            clusters.forEach { cluster ->
                val xPosDp = (centerXPx + (cluster.mapOffsetX * maxRadiusDp)).dp
                val yPosDp = (centerYPx + (cluster.mapOffsetY * maxRadiusDp)).dp

                val isSelected = (cluster.id == selectedClusterId)

                Box(
                    modifier = Modifier
                        .offset(x = xPosDp - 24.dp, y = yPosDp - 24.dp)
                        .size(48.dp)
                        .clickable { onSelectCluster(cluster.id) }
                        .testTag("map_node_${cluster.id}")
                )
            }
        }

        // Top Status Overlay
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.65f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(6.dp).background(MintLight, CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.gpsRadarSurveillance(lang),
                        color = MintLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Surface(
                color = Color.Black.copy(alpha = 0.65f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = AppStrings.tapNodesToInspect(lang),
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // Bottom Range Labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(AppStrings.radiusRange(lang), color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
            Text(AppStrings.regionalClustersCount(clusters.size, lang), color = MintLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * Regional Sector Grid Matrix View.
 * Organizes agricultural territories into a spatial grid with outbreak density meters.
 */
@Composable
fun RegionalSectorGridMatrix(
    clusters: List<AggregatedOutbreakCluster>,
    selectedClusterId: String?,
    onSelectCluster: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("regional_sector_grid_matrix"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = AppStrings.regionalSectorMatrix(lang),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary
            )
            Text(
                text = AppStrings.activeSectorsCount(clusters.size, lang),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }

        // Grid Matrix of Outbreak Sectors (2 columns)
        val chunked = clusters.chunked(2)
        chunked.forEach { rowClusters ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowClusters.forEach { cluster ->
                    val isSelected = (cluster.id == selectedClusterId)
                    val severityColor = when (cluster.maxSeverity) {
                        ThreatSeverity.CRITICAL -> CriticalRed
                        ThreatSeverity.HIGH -> WarningOrange
                        ThreatSeverity.MODERATE -> EarthAmber
                        ThreatSeverity.LOW -> LeafGreen
                    }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) ForestGreenPrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) ForestGreenPrimary else severityColor.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSelectCluster(cluster.id) }
                            .testTag("grid_sector_${cluster.id}")
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = severityColor.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = AppStrings.severityLabel(cluster.maxSeverity, lang),
                                        color = severityColor,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Surface(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = CircleShape
                                ) {
                                    Text(
                                        text = AppStrings.casesCount(cluster.reportCount, lang),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Text(
                                text = cluster.sectorName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = "${AppStrings.localizeCropName(cluster.primaryCrop, lang)} • ${cluster.primaryDisease}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            // Linear density bar
                            val progress = (cluster.reportCount / 10f).coerceIn(0.15f, 1f)
                            LinearProgressIndicator(
                                progress = { progress },
                                color = severityColor,
                                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                            )
                        }
                    }
                }
                if (rowClusters.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

/**
 * Inspection detail card shown when an outbreak cluster is tapped.
 */
@Composable
fun ClusterDetailInspectionCard(
    cluster: AggregatedOutbreakCluster,
    onReportClick: ((CropReport) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    val severityColor = when (cluster.maxSeverity) {
        ThreatSeverity.CRITICAL -> CriticalRed
        ThreatSeverity.HIGH -> WarningOrange
        ThreatSeverity.MODERATE -> EarthAmber
        ThreatSeverity.LOW -> LeafGreen
    }

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, severityColor.copy(alpha = 0.4f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("card_cluster_detail_inspection")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = ForestGreenPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = cluster.sectorName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "GPS: ${String.format("%.4f", cluster.latitude)}° N, ${String.format("%.4f", cluster.longitude)}° E • ${AppStrings.cropClusterRadius(AppStrings.localizeCropName(cluster.primaryCrop, lang), cluster.radiusKm, lang)}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                SeverityBadge(severity = cluster.maxSeverity)
            }

            // Primary Threat & Aggregated Count Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(severityColor.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Coronavirus,
                            contentDescription = null,
                            tint = severityColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = cluster.primaryDisease,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = AppStrings.confirmedDiagnosesTargetCrop(cluster.reportCount, AppStrings.localizeCropName(cluster.primaryCrop, lang), lang),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            // Regional Containment Advisory
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = ForestGreenPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = AppStrings.localizeAlertMessage(cluster.advisorySummary, lang),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }

            // List of Contributing Diagnosis Reports in this Cluster
            if (cluster.contributingReports.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "SAVED DIAGNOSES IN THIS SECTOR (${cluster.contributingReports.size})",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(cluster.contributingReports) { report ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                                modifier = Modifier
                                    .clickable { onReportClick?.invoke(report) }
                                    .testTag("contributing_report_${report.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(ForestGreenPrimary.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Eco,
                                            contentDescription = null,
                                            tint = ForestGreenPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "${report.cropName} (${report.variety})",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "Confidence: ${report.confidence}% • ${report.farmerName}",
                                            fontSize = 9.sp,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "View",
                                        tint = ForestGreenPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
