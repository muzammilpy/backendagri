package com.example.ui.screens

import androidx.compose.animation.*
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
import com.example.model.ReportStatus
import com.example.model.ThreatSeverity
import com.example.ui.components.CropSampleIllustration
import com.example.ui.components.SeverityBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.CropHealthViewModel
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtensionPortalScreen(
    viewModel: CropHealthViewModel,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    val allReports by viewModel.allReports.collectAsState()
    var selectedStatusTab by remember { mutableStateOf<ReportStatus?>(null) }
    var selectedReportForReview by remember { mutableStateOf<CropReport?>(null) }

    val filteredReports = remember(allReports, selectedStatusTab) {
        if (selectedStatusTab == null) allReports else allReports.filter { it.status == selectedStatusTab }
    }

    val pendingCount = remember(allReports) {
        allReports.count { it.status == ReportStatus.SUBMITTED_TO_EXPERT || it.status == ReportStatus.AI_DIAGNOSED }
    }
    val validatedCount = remember(allReports) {
        allReports.count { it.status == ReportStatus.EXPERT_VALIDATED }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        // Portal Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = ForestGreenPrimary,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = AppStrings.extensionWorkerAndLabPortal(lang),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            color = SafeGreenContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = AppStrings.validatedCountBadge(validatedCount, lang),
                                color = SafeGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = AppStrings.expertTriageAndLabValidation(lang),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AppStrings.extensionPortalHeaderDescription(lang),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Status Tabs
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = (selectedStatusTab == null),
                        onClick = { selectedStatusTab = null },
                        label = { Text(AppStrings.allCasesTab(allReports.size, lang)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ForestGreenPrimary,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    )
                }
                item {
                    FilterChip(
                        selected = (selectedStatusTab == ReportStatus.SUBMITTED_TO_EXPERT),
                        onClick = { selectedStatusTab = ReportStatus.SUBMITTED_TO_EXPERT },
                        label = { Text(AppStrings.pendingReviewTab(pendingCount, lang)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = WarningOrange,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    )
                }
                item {
                    FilterChip(
                        selected = (selectedStatusTab == ReportStatus.EXPERT_VALIDATED),
                        onClick = { selectedStatusTab = ReportStatus.EXPERT_VALIDATED },
                        label = { Text(AppStrings.confirmedTab(validatedCount, lang)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SafeGreen,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.minimumInteractiveComponentSize()
                    )
                }
            }
        }

        // Triage Cards List
        items(filteredReports) { report ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedReportForReview = report }
                    .testTag("triage_card_${report.id}")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CropSampleIllustration(
                                sampleKey = report.sampleImageKey,
                                customImageUri = report.customImageUri,
                                modifier = Modifier.size(42.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = report.farmerName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${AppStrings.localizeCropName(report.cropName, lang)} • ${report.villageRegion}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }

                        Surface(
                            color = when (report.status) {
                                ReportStatus.EXPERT_VALIDATED -> SafeGreenContainer
                                ReportStatus.SUBMITTED_TO_EXPERT -> WarningOrangeContainer
                                else -> MaterialTheme.colorScheme.surface
                            },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = AppStrings.localizeReportStatus(report.status, lang),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (report.status) {
                                    ReportStatus.EXPERT_VALIDATED -> SafeGreen
                                    ReportStatus.SUBMITTED_TO_EXPERT -> WarningOrange
                                    else -> ForestGreenPrimary
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Diagnosis summary
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = AppStrings.diseaseName(report.diseaseName, lang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenPrimary
                            )
                            Text(
                                text = AppStrings.confidenceAndStage(report.confidence, report.cropStage, lang),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        SeverityBadge(severity = report.severity)
                    }

                    if (report.expertNotes.isNotBlank()) {
                        Surface(
                            color = MintSoft.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(AppStrings.expertAgronomistNoteLabel(lang), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                                Text(report.expertNotes, fontSize = 12.sp, color = DeepJungle)
                            }
                        }
                    }

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { selectedReportForReview = report },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("btn_review_case_${report.id}")
                        ) {
                            Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (report.status == ReportStatus.EXPERT_VALIDATED) AppStrings.editValidationBtn(lang) else AppStrings.triageAndValidateBtn(lang), fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    // Review & Validation Dialog
    selectedReportForReview?.let { report ->
        var expertNameInput by remember { mutableStateOf(if (report.expertName.isNotBlank()) report.expertName else "Dr. Anita Rao (Agronomist)") }
        var expertNotesInput by remember { mutableStateOf(if (report.expertNotes.isNotBlank()) report.expertNotes else "Verified symptom morphology under field lens. Recommended immediate biological drench and systemic treatment.") }
        var statusSelection by remember { mutableStateOf(ReportStatus.EXPERT_VALIDATED) }

        AlertDialog(
            onDismissRequest = { selectedReportForReview = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = SafeGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(AppStrings.validateCropDiagnosisDialogTitle(lang), fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = AppStrings.specimenSummary(report.cropName, report.diseaseName, lang),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                    Text(AppStrings.reportedByFarmer(report.farmerName, report.villageRegion, lang), fontSize = 11.sp, color = Color.Gray)

                    OutlinedTextField(
                        value = expertNameInput,
                        onValueChange = { expertNameInput = it },
                        label = { Text(AppStrings.extensionOfficerNameLabel(lang)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = expertNotesInput,
                        onValueChange = { expertNotesInput = it },
                        label = { Text(AppStrings.officialAgronomicNotesLabel(lang)) },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(AppStrings.validationStatusLabel(lang), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { statusSelection = ReportStatus.EXPERT_VALIDATED },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (statusSelection == ReportStatus.EXPERT_VALIDATED) SafeGreen else Color.LightGray
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .minimumInteractiveComponentSize()
                        ) {
                            Text(AppStrings.confirmValidBtn(lang), fontSize = 11.sp)
                        }

                        Button(
                            onClick = { statusSelection = ReportStatus.IN_LAB_REVIEW },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (statusSelection == ReportStatus.IN_LAB_REVIEW) InfoBlue else Color.LightGray
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .minimumInteractiveComponentSize()
                        ) {
                            Text(AppStrings.labTestingBtn(lang), fontSize = 11.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateTriage(
                            reportId = report.id,
                            status = statusSelection,
                            notes = expertNotesInput,
                            expertName = expertNameInput
                        )
                        selectedReportForReview = null
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(AppStrings.saveAndIssueValidationBtn(lang))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { selectedReportForReview = null },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(AppStrings.cancelBtn(lang))
                }
            }
        )
    }
}
