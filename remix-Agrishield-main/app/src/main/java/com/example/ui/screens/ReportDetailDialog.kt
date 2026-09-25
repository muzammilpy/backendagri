package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.CropReport
import com.example.model.ReportStatus
import com.example.model.ThreatSeverity
import com.example.ui.components.AudioAdvisoryBar
import com.example.ui.components.CropSampleIllustration
import com.example.ui.components.SeverityBadge
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReportDetailDialog(
    report: CropReport,
    isSpeaking: Boolean,
    onSpeak: (String) -> Unit,
    onStopSpeak: () -> Unit,
    onUpdateFollowUp: (Long, Boolean, Boolean, Boolean, String) -> Unit,
    onSubmitToExtension: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    var day3 by remember(report.id) { mutableStateOf(report.followUpDay3Checked) }
    var day7 by remember(report.id) { mutableStateOf(report.followUpDay7Checked) }
    var day14 by remember(report.id) { mutableStateOf(report.followUpDay14Checked) }
    var followUpNote by remember(report.id) { mutableStateOf(report.followUpNotes) }

    val dateFormatted = remember(report.timestamp) {
        SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault()).format(Date(report.timestamp))
    }

    val audioScript = remember(report) {
        "Diagnosis for ${report.cropName}: ${report.diseaseName}. Severity is ${report.severity.label}. Biological management: ${report.ipmBiological}. Cultural action: ${report.ipmCultural}. Targeted chemical recommendation: ${report.ipmChemical}, dosage ${report.safeDosage}. Pre-harvest interval is ${report.preHarvestIntervalDays} days. Safety wear: ${report.safetyPpeInstructions}."
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("report_detail_dialog"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CleanGreenPrimary)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Assessment,
                            contentDescription = null,
                            tint = CleanGreenLime,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Crop Health Intelligence Card",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_close_detail_dialog")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Top Hero & Title Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CropSampleIllustration(
                                sampleKey = report.sampleImageKey,
                                customImageUri = report.customImageUri,
                                modifier = Modifier.size(80.dp)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                SeverityBadge(severity = report.severity)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = report.diseaseName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = report.scientificName,
                                    fontSize = 12.sp,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    color = ForestGreenPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${report.cropName} (${report.variety}) • ${report.confidence}% Confidence",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }

                    // Audio Advisory Player
                    AudioAdvisoryBar(
                        title = "${report.diseaseName} Treatment Advisory",
                        content = audioScript,
                        isSpeaking = isSpeaking,
                        onPlay = { onSpeak(audioScript) },
                        onStop = onStopSpeak
                    )

                    // Farm & Multi-Input Telemetry Bar
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CreamSurface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "FIELD CONDITIONS & MULTI-FACTOR INPUTS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EarthAmber
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Growth Stage", fontSize = 11.sp, color = Color.Gray)
                                    Text(report.cropStage, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Soil & Moisture", fontSize = 11.sp, color = Color.Gray)
                                    Text("${report.soilCondition} (${report.sensorMoisture.toInt()}%)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Column {
                                    Text("Weather Factors", fontSize = 11.sp, color = Color.Gray)
                                    Text(report.weatherFactors, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Trap Count / Temp", fontSize = 11.sp, color = Color.Gray)
                                    Text("${report.trapCount} moths • ${report.sensorTemp}°C", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }

                    // Cause & Weather Risk Correlation
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Analytics, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Epidemiology & Weather Correlation",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = report.causeSummary,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CloudQueue, contentDescription = null, tint = InfoBlue, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = report.weatherRiskCorrelation,
                                        fontSize = 12.sp,
                                        color = InfoBlue
                                    )
                                }
                            }
                        }
                    }

                    // INTEGRATED PEST MANAGEMENT (IPM) - Biological, Cultural, Chemical
                    Text(
                        text = "INTEGRATED PEST & DISEASE MANAGEMENT (IPM)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )

                    // 1. Biological Control
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SafeGreenContainer),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Spa, contentDescription = null, tint = SafeGreen, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("1. Biological & Bio-Pesticide Control", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SafeGreen)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(report.ipmBiological, fontSize = 13.sp, color = DeepJungle)
                        }
                    }

                    // 2. Cultural & Mechanical Practice
                    Card(
                        colors = CardDefaults.cardColors(containerColor = WheatBeige),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Agriculture, contentDescription = null, tint = EarthAmber, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("2. Cultural & Agronomic Management", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = EarthAmber)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(report.ipmCultural, fontSize = 13.sp, color = DeepJungle)
                        }
                    }

                    // 3. Targeted Chemical & Safety Dosage
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CriticalRedContainer.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Science, contentDescription = null, tint = CriticalRed, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("3. Targeted Chemical Intervention (Safe Threshold)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CriticalRed)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(report.ipmChemical, fontSize = 13.sp, color = DeepJungle, fontWeight = FontWeight.SemiBold)
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = Color.Black.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Safe Dosage", fontSize = 11.sp, color = Color.DarkGray, fontWeight = FontWeight.Bold)
                                    Text(report.safeDosage, fontSize = 12.sp, color = DeepJungle)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Pre-Harvest Interval (PHI)", fontSize = 11.sp, color = Color.DarkGray, fontWeight = FontWeight.Bold)
                                    Surface(color = CriticalRed, shape = RoundedCornerShape(6.dp)) {
                                        Text(
                                            text = "${report.preHarvestIntervalDays} Days PHI",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = CriticalRed, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "PPE: ${report.safetyPpeInstructions}",
                                    fontSize = 11.sp,
                                    color = DeepJungle
                                )
                            }
                        }
                    }

                    // Expert Triage & Validation Status
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Extension & Lab Validation", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Surface(
                                    color = if (report.status == ReportStatus.EXPERT_VALIDATED) SafeGreenContainer else WarningOrangeContainer,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = report.status.label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (report.status == ReportStatus.EXPERT_VALIDATED) SafeGreen else WarningOrange,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            if (report.status == ReportStatus.EXPERT_VALIDATED) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Verified by: ${report.expertName}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                                Text("Lab Notes: ${report.expertNotes}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            } else if (report.status == ReportStatus.AI_DIAGNOSED) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { onSubmitToExtension(report.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .minimumInteractiveComponentSize()
                                        .testTag("btn_submit_extension_lab")
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Submit Case to Extension Officer & Lab")
                                }
                            }
                        }
                    }

                    // Follow-Up Monitoring Checkpoints
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MintSoft.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Checklist, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Follow-Up Monitoring Schedule", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = ForestGreenPrimary)
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = day3,
                                        onCheckedChange = {
                                            day3 = it
                                            onUpdateFollowUp(report.id, day3, day7, day14, followUpNote)
                                        },
                                        modifier = Modifier.minimumInteractiveComponentSize()
                                    )
                                    Text("Day 3 Check", fontSize = 12.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = day7,
                                        onCheckedChange = {
                                            day7 = it
                                            onUpdateFollowUp(report.id, day3, day7, day14, followUpNote)
                                        },
                                        modifier = Modifier.minimumInteractiveComponentSize()
                                    )
                                    Text("Day 7 Check", fontSize = 12.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = day14,
                                        onCheckedChange = {
                                            day14 = it
                                            onUpdateFollowUp(report.id, day3, day7, day14, followUpNote)
                                        },
                                        modifier = Modifier.minimumInteractiveComponentSize()
                                    )
                                    Text("Day 14 Check", fontSize = 12.sp)
                                }
                            }

                            OutlinedTextField(
                                value = followUpNote,
                                onValueChange = {
                                    followUpNote = it
                                    onUpdateFollowUp(report.id, day3, day7, day14, it)
                                },
                                label = { Text("Field Recovery Notes & Ground Confirmation") },
                                placeholder = { Text("e.g. Sporulation dried, new shoots green...") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
                            )
                        }
                    }

                    Text(
                        text = "Recorded on $dateFormatted • Region: ${report.villageRegion}",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}
