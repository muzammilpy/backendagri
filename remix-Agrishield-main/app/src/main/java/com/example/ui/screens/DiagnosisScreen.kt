package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.CropReport
import com.example.model.ThreatSeverity
import com.example.ui.components.AudioAdvisoryBar
import com.example.ui.components.CameraPreviewComponent
import com.example.ui.components.CropLeafScannerOverlay
import com.example.ui.components.CropSampleIllustration
import com.example.ui.components.SeverityBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.CropHealthViewModel
import com.example.ui.viewmodel.DiagnosisUiState
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisScreen(
    viewModel: CropHealthViewModel,
    onNavigateToHotspots: () -> Unit,
    onNavigateToIpm: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lang = LocalAppLanguage.current

    val farmerName by viewModel.farmerName.collectAsState()
    val villageRegion by viewModel.villageRegion.collectAsState()
    val selectedCrop by viewModel.selectedCrop.collectAsState()
    val selectedVariety by viewModel.selectedVariety.collectAsState()
    val selectedStage by viewModel.selectedStage.collectAsState()
    val selectedSoil by viewModel.selectedSoil.collectAsState()
    val selectedWeather by viewModel.selectedWeather.collectAsState()
    val symptoms by viewModel.symptomDescription.collectAsState()
    val trapCount by viewModel.trapCount.collectAsState()
    val sensorMoisture by viewModel.sensorMoisture.collectAsState()
    val sensorTemp by viewModel.sensorTemp.collectAsState()
    val sampleImageKey by viewModel.sampleImageKey.collectAsState()
    val customImageUri by viewModel.customImageUri.collectAsState()
    val isImageProcessing by viewModel.isImageProcessing.collectAsState()
    val diagnosisState by viewModel.diagnosisState.collectAsState()
    val allReports by viewModel.allReports.collectAsState()
    val activeReport by viewModel.activeDetailReport.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val diagnosisSearchQuery by viewModel.diagnosisSearchQuery.collectAsState()
    val diagnosisCropFilter by viewModel.diagnosisCropFilter.collectAsState()
    var showCameraPreview by remember { mutableStateOf(false) }

    val filteredReports = remember(allReports, diagnosisSearchQuery, diagnosisCropFilter) {
        allReports.filter { report ->
            val matchesQuery = if (diagnosisSearchQuery.isBlank()) true else {
                val q = diagnosisSearchQuery.trim()
                report.diseaseName.contains(q, ignoreCase = true) ||
                report.scientificName.contains(q, ignoreCase = true) ||
                report.cropName.contains(q, ignoreCase = true) ||
                report.variety.contains(q, ignoreCase = true) ||
                report.symptomDescription.contains(q, ignoreCase = true) ||
                report.causeSummary.contains(q, ignoreCase = true) ||
                report.ipmBiological.contains(q, ignoreCase = true) ||
                report.ipmCultural.contains(q, ignoreCase = true) ||
                report.ipmChemical.contains(q, ignoreCase = true) ||
                report.status.label.contains(q, ignoreCase = true) ||
                report.severity.label.contains(q, ignoreCase = true) ||
                report.farmerName.contains(q, ignoreCase = true) ||
                report.villageRegion.contains(q, ignoreCase = true)
            }
            val matchesCrop = if (diagnosisCropFilter.isNullOrBlank() || diagnosisCropFilter == "All") true else {
                report.cropName.contains(diagnosisCropFilter!!, ignoreCase = true)
            }
            matchesQuery && matchesCrop
        }
    }

    // Activity Result Launcher for selecting crop leaf images from storage/gallery
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setCustomImage(context, uri)
        }
    }

    val cropPresets = listOf(
        Triple("Tomato", "Abhinav F1", "tomato_late_blight"),
        Triple("Rice (Paddy)", "Swarna Sub-1", "rice_blast"),
        Triple("Maize (Corn)", "DKC 9108", "maize_fall_armyworm"),
        Triple("Cotton", "Bt RCH-2", "cotton_pink_bollworm"),
        Triple("Wheat", "HD-2967", "wheat_rust")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("diagnosis_hero_banner"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = ForestGreenPrimary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(ForestGreenPrimary, LeafGreen)
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MintSoft.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = MintSoft,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = AppStrings.cropHealthVisionEngine(lang),
                                    color = MintSoft,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Surface(
                            color = EarthAmber,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = AppStrings.multimodalGemini(lang),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = AppStrings.cropLeafDiagnosisTitle(lang),
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AppStrings.cropLeafDiagnosisDescription(lang),
                        color = MintSoft,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Quick IPM Advisor Shortcut Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = SoftGreenCard.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.2.dp, CleanGreenPrimary.copy(alpha = 0.4f)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToIpm() }
                .testTag("card_shortcut_ipm_advisor")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = CleanGreenPrimary,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AccountTree,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = AppStrings.ipmDecisionTreeAdvisorTitle(lang),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CleanGreenPrimary
                        )
                        Text(
                            text = AppStrings.ipmDecisionTreeAdvisorSubtitle(lang),
                            fontSize = 11.sp,
                            color = MinimalTextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = CleanGreenPrimary,
                    modifier = Modifier.padding(start = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(AppStrings.openButton(lang), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                    }
                }
            }
        }

        // Section 1: Leaf Image Upload & Vision Preview Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_leaf_upload_section")
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = ForestGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.cropLeafSpecimenImageHeader(lang),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                    }

                    if (customImageUri != null) {
                        Surface(
                            color = LeafGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = AppStrings.customPhotoBadge(lang),
                                    color = ForestGreenPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Interactive Leaf Photo Preview Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            BorderStroke(
                                1.5.dp,
                                if (customImageUri != null) ForestGreenPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                            ),
                            RoundedCornerShape(16.dp)
                        )
                        .background(CreamSurface),
                    contentAlignment = Alignment.Center
                ) {
                    if (customImageUri != null) {
                        // User Uploaded Real Leaf Photo
                        AsyncImage(
                            model = customImageUri,
                            contentDescription = "Uploaded Crop Leaf",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        // Gradient shading overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f)),
                                        startY = 100f
                                    )
                                )
                        )

                        // Top info badge
                        Surface(
                            color = Color.Black.copy(alpha = 0.65f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Visibility, contentDescription = null, tint = MintSoft, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(AppStrings.targetCuticle(lang), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        // Bottom Actions on Preview
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilledTonalButton(
                                onClick = { viewModel.clearCustomImage() },
                                colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color.Black.copy(alpha = 0.6f)),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier
                                    .height(34.dp)
                                    .testTag("btn_remove_leaf_image")
                            ) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = AppStrings.clear(lang), tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(AppStrings.clear(lang), color = Color.White, fontSize = 11.sp)
                            }
                            Button(
                                onClick = { showCameraPreview = true },
                                colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier
                                    .height(34.dp)
                                    .testTag("btn_retake_leaf_image")
                            ) {
                                Icon(Icons.Default.PhotoCamera, contentDescription = AppStrings.retake(lang), tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(AppStrings.retake(lang), color = Color.White, fontSize = 11.sp)
                            }
                        }
                    } else {
                        // Preset Specimen Visual Illustration
                        CropSampleIllustration(
                            sampleKey = sampleImageKey,
                            modifier = Modifier.fillMaxSize()
                        )

                        Surface(
                            color = Color.Black.copy(alpha = 0.55f),
                            shape = RoundedCornerShape(topStart = 12.dp),
                            modifier = Modifier.align(Alignment.BottomEnd)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Icon(
                                    if (sampleImageKey == "auto_detect") Icons.Default.AutoAwesome else Icons.Default.Biotech,
                                    contentDescription = null,
                                    tint = MintSoft,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    if (sampleImageKey == "auto_detect") AppStrings.autoDetect(lang) else "Preset: $sampleImageKey",
                                    color = Color.White,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    // Scanner Laser Animation when Diagnosis is analyzing
                    if (diagnosisState is DiagnosisUiState.Analyzing) {
                        CropLeafScannerOverlay(modifier = Modifier.fillMaxSize())
                    }
                }

                // Image Upload & Camera Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { showCameraPreview = true },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("btn_open_camera_scanner")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.scan(lang),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = { photoPickerLauncher.launch("image/*") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("btn_upload_leaf_image")
                    ) {
                        if (isImageProcessing) {
                            CircularProgressIndicator(color = ForestGreenPrimary, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(AppStrings.loadingProgress(lang), fontSize = 12.sp)
                        } else {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp), tint = ForestGreenPrimary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.fromGallery(lang),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ForestGreenPrimary
                            )
                        }
                    }
                }

                // Specimen Presets Bar
                Column {
                    Text(
                        text = AppStrings.selectBenchmarkHeader(lang),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Universal Auto-Detect Chip (Bypasses hardcoded crop constraints)
                        item {
                            val isAutoDetectSelected = (customImageUri == null && sampleImageKey == "auto_detect")
                            Surface(
                                color = if (isAutoDetectSelected) MintSoft else MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp),
                                border = if (isAutoDetectSelected) BorderStroke(1.5.dp, ForestGreenPrimary) else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                                modifier = Modifier
                                    .clickable {
                                        viewModel.selectAutoDetectMode()
                                    }
                                    .testTag("preset_auto_detect")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = if (isAutoDetectSelected) ForestGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = AppStrings.autoDetect(lang),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isAutoDetectSelected) ForestGreenPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        items(cropPresets) { (crop, variety, key) ->
                            val isSelected = (customImageUri == null && sampleImageKey == key)
                            Surface(
                                color = if (isSelected) MintSoft else MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp),
                                border = if (isSelected) BorderStroke(1.5.dp, ForestGreenPrimary) else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                                modifier = Modifier
                                    .clickable {
                                        when (key) {
                                            "tomato_late_blight" -> viewModel.selectPresetSample(
                                                "Tomato", "Abhinav F1", "Fruiting Stage",
                                                "Loamy, High Moisture (80%)", "Continuous rain, 90% humidity",
                                                "Dark water-soaked brown lesions on leaves and fruit with white fungal downy growth on undersides.",
                                                4, 82f, 24.5f, "tomato_late_blight"
                                            )
                                            "rice_blast" -> viewModel.selectPresetSample(
                                                "Rice (Paddy)", "Swarna Sub-1", "Panicle Initiation",
                                                "Submerged Clay", "Overcast, 86% humidity, morning dew",
                                                "Spindle-shaped diamond eye lesions with grey centers and dark reddish-brown borders on leaf blades.",
                                                2, 94f, 26.8f, "rice_blast"
                                            )
                                            "maize_fall_armyworm" -> viewModel.selectPresetSample(
                                                "Maize (Corn)", "DKC 9108", "Whorl / Vegetative",
                                                "Sandy Loam", "Warm & Dry, 31°C",
                                                "Extensive window-pane feeding on whorl leaves with moist sawdust-like frass inside the leaf funnel.",
                                                16, 45f, 31.0f, "maize_fall_armyworm"
                                            )
                                            "cotton_pink_bollworm" -> viewModel.selectPresetSample(
                                                "Cotton", "Bt RCH-2", "Squaring & Boll Formation",
                                                "Black Cotton Soil (Clayey)", "Humid evening, 29°C",
                                                "Rosetted flowers that fail to open properly; exit holes on tender green bolls with staining.",
                                                9, 58f, 29.5f, "cotton_pink_bollworm"
                                            )
                                            "wheat_rust" -> viewModel.selectPresetSample(
                                                "Wheat", "HD-2967", "Tillering to Boot Stage",
                                                "Alluvial Loam", "Cool foggy mornings, 14°C",
                                                "Linear stripes of bright yellow powdery pustules arranged in parallel lines on leaf blades.",
                                                1, 62f, 15.0f, "wheat_rust"
                                            )
                                        }
                                    }
                                    .testTag("preset_${key}")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        when (key) {
                                            "tomato_late_blight" -> Icons.Default.Coronavirus
                                            "rice_blast" -> Icons.Default.Grass
                                            "maize_fall_armyworm" -> Icons.Default.BugReport
                                            "cotton_pink_bollworm" -> Icons.Default.PestControl
                                            else -> Icons.Default.Grain
                                        },
                                        contentDescription = null,
                                        tint = if (isSelected) ForestGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = AppStrings.cropName(crop, lang),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) ForestGreenPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Multi-Factor Agronomy & Field Context
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = AppStrings.fieldConditionsHeader(lang),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )

                    Surface(
                        color = ForestGreenPrimary,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = selectedCrop.ifBlank { AppStrings.autoDetect(lang) },
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                // Crop & Variety Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = selectedCrop,
                        onValueChange = { viewModel.updateForm(crop = it) },
                        label = { Text(AppStrings.cropTypeLabel(lang)) },
                        placeholder = { Text(AppStrings.cropTypePlaceholder(lang)) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_crop_type"),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = selectedVariety,
                        onValueChange = { viewModel.updateForm(variety = it) },
                        label = { Text(AppStrings.varietyLabel(lang)) },
                        placeholder = { Text(AppStrings.varietyPlaceholder(lang)) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_crop_variety"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Growth Stage & Soil Condition Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = selectedStage,
                        onValueChange = { viewModel.updateForm(stage = it) },
                        label = { Text(AppStrings.growthStageLabel(lang)) },
                        placeholder = { Text(AppStrings.growthStagePlaceholder(lang)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = selectedSoil,
                        onValueChange = { viewModel.updateForm(soil = it) },
                        label = { Text(AppStrings.soilConditionLabel(lang)) },
                        placeholder = { Text(AppStrings.soilConditionPlaceholder(lang)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Observed Symptoms Text Field
                OutlinedTextField(
                    value = symptoms,
                    onValueChange = { viewModel.updateForm(symptoms = it) },
                    label = { Text(AppStrings.observedSymptomsLabel(lang)) },
                    placeholder = { Text(AppStrings.observedSymptomsPlaceholder(lang)) },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_symptoms"),
                    shape = RoundedCornerShape(12.dp)
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                // Weather & Sensor Telemetry Inputs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.microclimateTelemetryHeader(lang),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EarthAmber,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = {
                            val weather = viewModel.currentMicroclimate.value
                            viewModel.updateForm(
                                weather = "${weather.description} (RH: ${weather.relativeHumidityPercent.toInt()}%, Temp: ${weather.temperatureC}°C, Rain: ${weather.rainfallMm}mm)",
                                moisture = weather.relativeHumidityPercent,
                                temp = weather.temperatureC
                            )
                        },
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, ForestGreenPrimary.copy(alpha = 0.4f)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = ForestGreenPrimary.copy(alpha = 0.08f),
                            contentColor = ForestGreenPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(Alignment.CenterVertically)
                            .minimumInteractiveComponentSize()
                            .testTag("btn_sync_weather_telemetry")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = ForestGreenPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.syncLiveWeather(lang),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ForestGreenPrimary,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = selectedWeather,
                    onValueChange = { viewModel.updateForm(weather = it) },
                    label = { Text(AppStrings.recentWeatherLabel(lang)) },
                    placeholder = { Text(AppStrings.recentWeatherPlaceholder(lang)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Trap Count & Sensor Sliders
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(AppStrings.trapCountLabel(trapCount, lang), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        if (trapCount >= 8) {
                            Text(AppStrings.thresholdExceeded(lang), fontSize = 11.sp, color = CriticalRed, fontWeight = FontWeight.Bold)
                        }
                    }
                    Slider(
                        value = trapCount.toFloat(),
                        onValueChange = { viewModel.updateForm(trap = it.toInt()) },
                        valueRange = 0f..40f,
                        steps = 39,
                        modifier = Modifier.testTag("slider_trap_count")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(AppStrings.canopyMoistureLabel(sensorMoisture.toInt(), lang), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text(AppStrings.tempLabel(sensorTemp.toInt(), lang), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Slider(
                        value = sensorMoisture,
                        onValueChange = { viewModel.updateForm(moisture = it) },
                        valueRange = 10f..100f,
                        modifier = Modifier.testTag("slider_moisture")
                    )
                }

                // Diagnose Button
                Button(
                    onClick = { viewModel.runDiagnosis() },
                    enabled = diagnosisState !is DiagnosisUiState.Analyzing,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("btn_run_diagnosis")
                ) {
                    if (diagnosisState is DiagnosisUiState.Analyzing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(AppStrings.diagnosingLeafLesionsWithAi(lang), fontSize = 14.sp)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (customImageUri != null) AppStrings.diagnoseUploadedLeafWithAi(lang) else AppStrings.runAiCropHealthDiagnosis(lang),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Active Diagnosis State Banner if Error
        if (diagnosisState is DiagnosisUiState.Error) {
            Surface(
                color = CriticalRedContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = CriticalRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text((diagnosisState as DiagnosisUiState.Error).message, color = CriticalRed, fontSize = 13.sp)
                }
            }
        }

        // Active Diagnosis State Banner if Invalid Non-Plant Object Detected
        if (diagnosisState is DiagnosisUiState.InvalidPlant) {
            val invalidState = diagnosisState as DiagnosisUiState.InvalidPlant
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CriticalRedContainer),
                border = BorderStroke(1.5.dp, CriticalRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_invalid_plant_alert")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = CriticalRed,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.HideImage,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = AppStrings.invalidPlantTitle(lang),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = CriticalRed
                            )
                            Text(
                                text = AppStrings.invalidPlantDiagnosisFailed(lang),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    if (invalidState.reason.isNotBlank() && invalidState.reason != AppStrings.invalidPlantDiagnosisFailed(lang)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = invalidState.reason,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.resetPhotoAndDiagnosis()
                                showCameraPreview = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .minimumInteractiveComponentSize()
                                .testTag("btn_retake_photo_invalid")
                        ) {
                            Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.retakePhoto(lang),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.resetPhotoAndDiagnosis()
                                photoPickerLauncher.launch("image/*")
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .minimumInteractiveComponentSize()
                                .testTag("btn_gallery_invalid")
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp), tint = ForestGreenPrimary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.fromGallery(lang),
                                fontSize = 12.sp,
                                color = ForestGreenPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Active Diagnosis Success Card
        if (diagnosisState is DiagnosisUiState.Success) {
            val report = (diagnosisState as DiagnosisUiState.Success).report
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CreamSurface),
                border = BorderStroke(2.dp, ForestGreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_latest_diagnosis_result")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = ForestGreenPrimary,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(AppStrings.diagnosisComplete(lang), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        SeverityBadge(severity = report.severity)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CropSampleIllustration(
                            sampleKey = report.sampleImageKey,
                            customImageUri = report.customImageUri,
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    color = SafeGreenContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${report.cropName} • ${report.variety}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreenPrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = AppStrings.diseaseName(report.diseaseName, lang),
                                fontSize = 17.sp,
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
                                text = AppStrings.confidenceAndPhi(report.confidence, report.preHarvestIntervalDays, lang),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = EarthAmber
                            )
                        }
                    }

                    // Symptoms Observed / Detected
                    if (report.symptomDescription.isNotBlank()) {
                        Surface(
                            color = Color(0xFFF1F8E9),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    tint = ForestGreenPrimary,
                                    modifier = Modifier.size(16.dp).padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "Observed Lesions & Foliar Symptoms",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreenPrimary
                                    )
                                    Text(
                                        text = report.symptomDescription,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }

                    // Cause Summary
                    Text(
                        text = report.causeSummary,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )

                    // Audio Advisory
                    val audioScript = "Diagnosis for ${report.cropName}: ${report.diseaseName}. Severity is ${report.severity.label}. Biological management: ${report.ipmBiological}. Cultural action: ${report.ipmCultural}. Targeted chemical recommendation: ${report.ipmChemical}, dosage ${report.safeDosage}. Pre-harvest interval is ${report.preHarvestIntervalDays} days. Safety wear: ${report.safetyPpeInstructions}."
                    AudioAdvisoryBar(
                        title = "${AppStrings.diseaseName(report.diseaseName, lang)} Audio Advisory",
                        content = audioScript,
                        isSpeaking = isSpeaking,
                        onPlay = { viewModel.speakAdvisory(audioScript) },
                        onStop = { viewModel.stopSpeaking() }
                    )

                    // Action Button to View Full Prescription
                    Button(
                        onClick = { viewModel.setActiveReport(report) },
                        colors = ButtonDefaults.buttonColors(containerColor = CleanGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("btn_view_full_prescription")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(AppStrings.viewCompletePrescriptionButton(lang), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section 4: Recent Farm Diagnoses & Triage Records
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.recentFarmDiagnosesHeader(allReports.size, lang),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary
                )
                TextButton(
                    onClick = onNavigateToHotspots,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(AppStrings.viewHotspotMap(lang), fontSize = 12.sp, color = ForestGreenPrimary)
                }
            }

            // Search Bar for Diagnosis Records
            OutlinedTextField(
                value = diagnosisSearchQuery,
                onValueChange = { viewModel.setDiagnosisSearchQuery(it) },
                placeholder = { Text(AppStrings.searchPreviousDiagnosesPlaceholder(lang), fontSize = 13.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search diagnoses",
                        tint = ForestGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (diagnosisSearchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.setDiagnosisSearchQuery("") },
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search", modifier = Modifier.size(16.dp))
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ForestGreenPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_diagnosis_history_search")
            )

            // Crop Filter Chips for History
            val historyCropChips = listOf("All", "Tomato", "Rice (Paddy)", "Maize (Corn)", "Cotton", "Wheat")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                historyCropChips.forEach { cropChip ->
                    val isSelected = (diagnosisCropFilter == null && cropChip == "All") || diagnosisCropFilter == cropChip
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setDiagnosisCropFilter(if (cropChip == "All") null else cropChip) },
                        label = {
                            Text(
                                text = AppStrings.cropName(cropChip, lang),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ForestGreenPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) ForestGreenPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            enabled = true,
                            selected = isSelected
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("chip_diagnosis_filter_$cropChip")
                    )
                }
            }

            // Results count status
            if (diagnosisSearchQuery.isNotBlank() || diagnosisCropFilter != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.foundRecordsCount(filteredReports.size, allReports.size, lang),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                    TextButton(
                        onClick = { viewModel.clearDiagnosisFilters() },
                        modifier = Modifier.minimumInteractiveComponentSize().testTag("btn_clear_diagnosis_filters")
                    ) {
                        Text(AppStrings.resetFilters(lang), fontSize = 11.sp, color = EarthAmber)
                    }
                }
            }

            if (allReports.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Grass, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(AppStrings.noCropReportsRecorded(lang), color = Color.Gray, fontSize = 13.sp)
                    }
                }
            } else if (filteredReports.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth().testTag("card_no_diagnosis_search_results")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.SearchOff, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = AppStrings.noMatchingDiagnoses(lang),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = AppStrings.tryAdjustingKeywords(lang),
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = { viewModel.clearDiagnosisFilters() },
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Text(AppStrings.clearFilters(lang), fontSize = 12.sp)
                        }
                    }
                }
            } else {
                filteredReports.forEach { report ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setActiveReport(report) }
                            .testTag("report_item_${report.id}")
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
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${AppStrings.cropName(report.cropName, lang)} • ${report.variety}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreenPrimary
                                    )
                                    SeverityBadge(severity = report.severity)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = AppStrings.diseaseName(report.diseaseName, lang),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Status: ${AppStrings.reportStatus(report.status, lang)} • PHI: ${report.preHarvestIntervalDays}d",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Show Detail Dialog when a report is selected
    activeReport?.let { report ->
        ReportDetailDialog(
            report = report,
            isSpeaking = isSpeaking,
            onSpeak = { viewModel.speakAdvisory(it) },
            onStopSpeak = { viewModel.stopSpeaking() },
            onUpdateFollowUp = { id, d3, d7, d14, notes -> viewModel.updateFollowUp(id, d3, d7, d14, notes) },
            onSubmitToExtension = { id -> viewModel.updateTriage(id, com.example.model.ReportStatus.SUBMITTED_TO_EXPERT, "Submitted from field app", "Pending Assignment") },
            onDismiss = { viewModel.setActiveReport(null) }
        )
    }

    // Invalid Plant Modal Dialog
    if (diagnosisState is DiagnosisUiState.InvalidPlant) {
        val invalidReason = (diagnosisState as DiagnosisUiState.InvalidPlant).reason
        AlertDialog(
            onDismissRequest = { viewModel.resetDiagnosisState() },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = CriticalRed,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = AppStrings.invalidPlantTitle(lang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = CriticalRed,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = AppStrings.invalidPlantDiagnosisFailed(lang),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetPhotoAndDiagnosis()
                        showCameraPreview = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("dialog_btn_retake_photo")
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(AppStrings.retakePhoto(lang), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.resetPhotoAndDiagnosis() },
                    modifier = Modifier.minimumInteractiveComponentSize().testTag("dialog_btn_dismiss_invalid")
                ) {
                    Text(AppStrings.cancel(lang), color = MaterialTheme.colorScheme.outline)
                }
            }
        )
    }

    // Fullscreen CameraX Live Preview Scanner Dialog
    if (showCameraPreview) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showCameraPreview = false },
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true
            )
        ) {
            CameraPreviewComponent(
                onImageCaptured = { uri ->
                    viewModel.setCustomImage(context, uri)
                    showCameraPreview = false
                },
                onClose = { showCameraPreview = false },
                onOpenGallery = {
                    showCameraPreview = false
                    photoPickerLauncher.launch("image/*")
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
