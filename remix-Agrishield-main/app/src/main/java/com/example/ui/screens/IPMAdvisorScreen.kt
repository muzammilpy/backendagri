package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AltRoute
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.IPMDecisionTreeData
import com.example.model.*
import com.example.ui.components.SeverityBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.CropHealthViewModel
import com.example.util.AppStrings
import com.example.util.LocalAppLanguage

enum class IPMAdvisorTab {
    DECISION_TREE,
    PROTOCOL_LIBRARY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IPMAdvisorScreen(
    viewModel: CropHealthViewModel,
    onNavigateToDiagnosis: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current
    var selectedTab by remember { mutableStateOf(IPMAdvisorTab.DECISION_TREE) }

    val currentNodeId by viewModel.currentIpmNodeId.collectAsState()
    val history by viewModel.ipmHistory.collectAsState()
    val currentRecommendation by viewModel.currentIpmRecommendation.collectAsState()
    val searchQuery by viewModel.ipmSearchQuery.collectAsState()
    val selectedCropFilter by viewModel.ipmSelectedCropFilter.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    val currentNode = IPMDecisionTreeData.decisionTreeNodes[currentNodeId]
        ?: IPMDecisionTreeData.decisionTreeNodes["root"]!!

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MinimalBackground)
    ) {
        // Top Header Banner
        Surface(
            color = MinimalSurface,
            border = BorderStroke(1.dp, MinimalBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = CleanGreenLime.copy(alpha = 0.5f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AccountTree,
                                    contentDescription = AppStrings.ipmAdvisorTitle(lang),
                                    tint = CleanGreenPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = AppStrings.ipmAdvisorTitle(lang),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MinimalTextPrimary
                            )
                            Text(
                                text = AppStrings.ipmAdvisorSubtitle(lang),
                                fontSize = 12.sp,
                                color = MinimalTextSecondary
                            )
                        }
                    }

                    if (currentRecommendation != null) {
                        IconButton(
                            onClick = {
                                if (isSpeaking) {
                                    viewModel.stopSpeaking()
                                } else {
                                    val textToRead = "IPM Advisory for ${currentRecommendation?.diseaseName}. ${currentRecommendation?.keySymptomsSummary}. Cultural control: ${currentRecommendation?.culturalActions?.firstOrNull()?.instruction ?: ""}. Bio-control: ${currentRecommendation?.biologicalActions?.firstOrNull()?.instruction ?: ""}"
                                    viewModel.speakAdvisory(textToRead)
                                }
                            },
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("btn_ipm_speech_advisory")
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = if (isSpeaking) AppStrings.stopAudio(lang) else AppStrings.listenAudioAdvisory(lang),
                                tint = CleanGreenPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Unified IPM Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setIpmSearchQuery(it) },
                    placeholder = { Text(AppStrings.ipmSearchPlaceholder(lang), fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = CleanGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { viewModel.setIpmSearchQuery("") },
                                modifier = Modifier.minimumInteractiveComponentSize().testTag("btn_clear_ipm_search")
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CleanGreenPrimary,
                        unfocusedBorderColor = MinimalBorder,
                        focusedContainerColor = MinimalSurfaceVariant.copy(alpha = 0.5f),
                        unfocusedContainerColor = MinimalSurfaceVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_ipm_search")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Mode Tabs Switcher
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MinimalSurfaceVariant)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        color = if (selectedTab == IPMAdvisorTab.DECISION_TREE) CleanGreenPrimary else Color.Transparent,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = IPMAdvisorTab.DECISION_TREE }
                            .testTag("tab_ipm_decision_tree")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.AltRoute,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (selectedTab == IPMAdvisorTab.DECISION_TREE) Color.White else MinimalTextSecondary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.decisionTreeTab(lang),
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == IPMAdvisorTab.DECISION_TREE) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == IPMAdvisorTab.DECISION_TREE) Color.White else MinimalTextSecondary
                            )
                        }
                    }

                    Surface(
                        color = if (selectedTab == IPMAdvisorTab.PROTOCOL_LIBRARY) CleanGreenPrimary else Color.Transparent,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = IPMAdvisorTab.PROTOCOL_LIBRARY }
                            .testTag("tab_ipm_protocol_library")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (selectedTab == IPMAdvisorTab.PROTOCOL_LIBRARY) Color.White else MinimalTextSecondary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.protocolLibraryTab(lang),
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == IPMAdvisorTab.PROTOCOL_LIBRARY) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == IPMAdvisorTab.PROTOCOL_LIBRARY) Color.White else MinimalTextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Tab Content
        Crossfade(
            targetState = selectedTab,
            label = "ipm_tab_crossfade"
        ) { tab ->
            when (tab) {
                IPMAdvisorTab.DECISION_TREE -> {
                    if (currentRecommendation != null) {
                        IPMRecommendationDetailView(
                            recommendation = currentRecommendation!!,
                            onBackToTree = { viewModel.clearIpmRecommendation() },
                            onResetTree = { viewModel.resetIpmDecisionTree() },
                            onApplyToDiagnosis = {
                                viewModel.applyIpmToDiagnosis(currentRecommendation!!)
                                onNavigateToDiagnosis()
                            }
                        )
                    } else {
                        IPMDecisionTreeInteractiveView(
                            currentNode = currentNode,
                            history = history,
                            searchQuery = searchQuery,
                            onOptionSelected = { option -> viewModel.selectIpmOption(currentNode, option) },
                            onBack = { viewModel.navigateIpmBack() },
                            onReset = { viewModel.resetIpmDecisionTree() },
                            onJumpToStep = { stepIndex -> viewModel.jumpToIpmHistoryStep(stepIndex) },
                            onSelectRecommendation = { rec ->
                                viewModel.selectIpmRecommendationDirectly(rec.diseaseId)
                            }
                        )
                    }
                }
                IPMAdvisorTab.PROTOCOL_LIBRARY -> {
                    IPMProtocolLibraryView(
                        searchQuery = searchQuery,
                        selectedCrop = selectedCropFilter,
                        onSearchChange = { viewModel.setIpmSearchQuery(it) },
                        onCropSelect = { viewModel.setIpmCropFilter(it) },
                        onSelectRecommendation = { rec ->
                            viewModel.selectIpmRecommendationDirectly(rec.diseaseId)
                            selectedTab = IPMAdvisorTab.DECISION_TREE
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun IPMDecisionTreeInteractiveView(
    currentNode: DecisionTreeNode,
    history: List<DecisionTreeHistoryStep>,
    searchQuery: String = "",
    onOptionSelected: (DecisionTreeOption) -> Unit,
    onBack: () -> Unit,
    onReset: () -> Unit,
    onJumpToStep: (Int) -> Unit,
    onSelectRecommendation: (IPMProtocolRecommendation) -> Unit = {}
) {
    val lang = LocalAppLanguage.current
    val searchMatches = remember(searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else IPMDecisionTreeData.searchRecommendations(searchQuery)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 32.dp)
    ) {
        // Direct Search Matches (When user searches in Decision Tree mode)
        if (searchQuery.isNotBlank()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MinimalSurface),
                    border = BorderStroke(1.dp, CleanGreenPrimary.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth().testTag("card_ipm_search_matches_container")
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = CleanGreenPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = AppStrings.directSearchMatches(searchMatches.size, lang),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CleanGreenPrimary
                                )
                            }
                        }

                        if (searchMatches.isEmpty()) {
                            Text(
                                text = AppStrings.noIpmMatches(searchQuery, lang),
                                fontSize = 12.sp,
                                color = MinimalTextSecondary
                            )
                        } else {
                            searchMatches.take(3).forEach { matchRec ->
                                Surface(
                                    color = MinimalSurfaceVariant,
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, MinimalBorder),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onSelectRecommendation(matchRec) }
                                        .testTag("quick_match_rec_${matchRec.diseaseId}")
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = matchRec.diseaseName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MinimalTextPrimary
                                            )
                                            Text(
                                                text = "${AppStrings.cropsLabel(lang)}: ${matchRec.targetCrops.map { AppStrings.localizeCropName(it, lang) }.joinToString(", ")}",
                                                fontSize = 11.sp,
                                                color = CleanGreenPrimary
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = "Open",
                                            tint = CleanGreenPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // Breadcrumbs & History Trail
        if (history.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MinimalSurface)
                        .border(BorderStroke(1.dp, MinimalBorder), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timeline,
                                contentDescription = null,
                                tint = CleanGreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.decisionPathHeader(history.size, lang),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CleanGreenPrimary
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            TextButton(
                                onClick = onBack,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.minimumInteractiveComponentSize().testTag("btn_ipm_back_step")
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(AppStrings.backBtn(lang), fontSize = 11.sp)
                            }
                            TextButton(
                                onClick = onReset,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.minimumInteractiveComponentSize().testTag("btn_ipm_reset_tree")
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(AppStrings.resetBtn(lang), fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MinimalSurfaceVariant,
                            border = BorderStroke(1.dp, MinimalBorder),
                            modifier = Modifier.clickable { onReset() }
                        ) {
                            Text(
                                text = "🌱 ${AppStrings.rootStep(lang)}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MinimalTextSecondary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        history.forEachIndexed { index, step ->
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = MinimalTextMuted,
                                modifier = Modifier.size(12.dp)
                            )

                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (index == history.lastIndex) CleanGreenLime.copy(alpha = 0.4f) else MinimalSurfaceVariant,
                                border = BorderStroke(1.dp, if (index == history.lastIndex) CleanGreenPrimary.copy(alpha = 0.3f) else MinimalBorder),
                                modifier = Modifier.clickable { onJumpToStep(index) }
                            ) {
                                Text(
                                    text = step.selectedOption.label.take(24) + if (step.selectedOption.label.length > 24) "…" else "",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (index == history.lastIndex) CleanGreenPrimary else MinimalTextSecondary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Current Question Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MinimalSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, CleanGreenPrimary.copy(alpha = 0.3f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = CleanGreenPrimary.copy(alpha = 0.1f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = CleanGreenPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Text(
                            text = AppStrings.diagnosticQuestionHeader(lang),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CleanGreenPrimary,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentNode.question,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MinimalTextPrimary,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = MinimalSurfaceVariant.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = CleanGreenPrimary,
                                modifier = Modifier.size(16.dp).padding(top = 2.dp)
                            )
                            Text(
                                text = currentNode.explanation,
                                fontSize = 12.sp,
                                color = MinimalTextSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // Section Title: Available Choices
        item {
            Text(
                text = AppStrings.selectMatchingObservation(lang),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MinimalTextSecondary
            )
        }

        // Branching Option Cards
        items(currentNode.options) { option ->
            DecisionTreeOptionCard(
                option = option,
                onClick = { onOptionSelected(option) }
            )
        }
    }
}

@Composable
fun DecisionTreeOptionCard(
    option: DecisionTreeOption,
    onClick: () -> Unit
) {
    val lang = LocalAppLanguage.current
    val isFinal = option.finalRecommendationId != null

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isFinal) SoftGreenCard.copy(alpha = 0.4f) else MinimalSurface
        ),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            1.2.dp,
            if (isFinal) CleanGreenPrimary.copy(alpha = 0.4f) else MinimalBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("ipm_option_${option.optionId}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (isFinal) {
                        Surface(
                            color = CleanGreenPrimary,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = AppStrings.protocolReadyBadge(lang),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = option.label,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MinimalTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = option.subtext,
                    fontSize = 12.sp,
                    color = MinimalTextSecondary,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Surface(
                shape = CircleShape,
                color = if (isFinal) CleanGreenPrimary else MinimalSurfaceVariant,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isFinal) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Select option",
                        tint = if (isFinal) Color.White else MinimalTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun IPMRecommendationDetailView(
    recommendation: IPMProtocolRecommendation,
    onBackToTree: () -> Unit,
    onResetTree: () -> Unit,
    onApplyToDiagnosis: () -> Unit
) {
    val lang = LocalAppLanguage.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 36.dp)
    ) {
        // Navigation Back & Reset Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onBackToTree,
                    modifier = Modifier.minimumInteractiveComponentSize().testTag("btn_ipm_back_to_tree")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(AppStrings.backToTreeBtn(lang))
                }

                TextButton(
                    onClick = onResetTree,
                    modifier = Modifier.minimumInteractiveComponentSize().testTag("btn_ipm_new_tree")
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(AppStrings.newDecisionTreeBtn(lang))
                }
            }
        }

        // Diagnosis Summary Hero Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MinimalSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, CleanGreenPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth().testTag("card_ipm_protocol_hero")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = CleanGreenLime.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = AppStrings.ipmProtocolVerified(lang),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CleanGreenPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        SeverityBadge(severity = recommendation.severityLevel)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = recommendation.diseaseName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MinimalTextPrimary
                    )

                    Text(
                        text = recommendation.scientificName,
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                        color = MinimalTextSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tags row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MinimalSurfaceVariant,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "${AppStrings.typeLabel(lang)}: ${recommendation.pathogenType}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MinimalTextSecondary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        if (recommendation.preHarvestIntervalDays != null) {
                            Surface(
                                color = SoftWarningCard,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "${AppStrings.phiDays(recommendation.preHarvestIntervalDays!!, lang)}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SoftWarningText,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${AppStrings.targetCrops(lang)}: ${recommendation.targetCrops.map { AppStrings.localizeCropName(it, lang) }.joinToString(", ")}",
                        fontSize = 12.sp,
                        color = MinimalTextMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = MinimalSurfaceVariant.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = AppStrings.foliarFieldSymptoms(lang),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = EarthAmber
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = recommendation.keySymptomsSummary,
                                fontSize = 12.sp,
                                color = MinimalTextPrimary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // Action: Apply to AI Diagnosis
        item {
            Button(
                onClick = onApplyToDiagnosis,
                colors = ButtonDefaults.buttonColors(containerColor = CleanGreenPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_apply_ipm_to_diagnosis")
            ) {
                Icon(Icons.Default.HealthAndSafety, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.applyToDiagnosisBtn(lang),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 4-Tier IPM Strategy Section Header
        item {
            Text(
                text = AppStrings.fourTierIpmHeader(lang),
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CleanGreenPrimary,
                letterSpacing = 0.5.sp
            )
        }

        // Tier 1: Cultural & Agronomic
        item {
            IPMTierCard(
                tier = IPMInterventionTier.CULTURAL,
                actions = recommendation.culturalActions,
                icon = Icons.Default.Agriculture,
                badgeColor = CleanGreenPrimary,
                containerColor = SoftGreenCard.copy(alpha = 0.3f)
            )
        }

        // Tier 2: Mechanical & Physical Traps
        item {
            IPMTierCard(
                tier = IPMInterventionTier.MECHANICAL,
                actions = recommendation.mechanicalActions,
                icon = Icons.Default.FilterFrames,
                badgeColor = EarthAmber,
                containerColor = SoftWarningCard.copy(alpha = 0.3f)
            )
        }

        // Tier 3: Biological & Botanical
        item {
            IPMTierCard(
                tier = IPMInterventionTier.BIOLOGICAL,
                actions = recommendation.biologicalActions,
                icon = Icons.Default.Spa,
                badgeColor = SoftPurpleText,
                containerColor = SoftPurpleCard.copy(alpha = 0.3f)
            )
        }

        // Tier 4: Chemical & Targeted Rationals (Last Resort)
        item {
            IPMTierCard(
                tier = IPMInterventionTier.CHEMICAL_RATIONAL,
                actions = recommendation.chemicalActions,
                icon = Icons.Default.Science,
                badgeColor = CriticalRed,
                containerColor = SoftCriticalCard.copy(alpha = 0.3f)
            )
        }

        // Prevention Checklist
        if (recommendation.preventionChecklist.isNotEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MinimalSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MinimalBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Checklist, contentDescription = null, tint = CleanGreenPrimary, modifier = Modifier.size(18.dp))
                            Text(
                                text = AppStrings.agronomistPreventionChecklist(lang),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MinimalTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        recommendation.preventionChecklist.forEach { checkItem ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = CleanGreenPrimary,
                                    modifier = Modifier.size(14.dp).padding(top = 2.dp)
                                )
                                Text(
                                    text = checkItem,
                                    fontSize = 12.sp,
                                    color = MinimalTextSecondary,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IPMTierCard(
    tier: IPMInterventionTier,
    actions: List<IPMActionItem>,
    icon: ImageVector,
    badgeColor: Color,
    containerColor: Color
) {
    val lang = LocalAppLanguage.current
    Card(
        colors = CardDefaults.cardColors(containerColor = MinimalSurface),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, MinimalBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = containerColor,
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = badgeColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = AppStrings.localizeTierTitle(tier, lang),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MinimalTextPrimary
                    )
                    Text(
                        text = AppStrings.localizeTierDescription(tier, lang),
                        fontSize = 11.sp,
                        color = MinimalTextMuted,
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MinimalBorder)
            Spacer(modifier = Modifier.height(10.dp))

            if (actions.isEmpty()) {
                Text(
                    text = AppStrings.noSpecificActions(lang),
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = MinimalTextMuted
                )
            } else {
                actions.forEachIndexed { index, action ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MinimalSurfaceVariant.copy(alpha = 0.5f))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = action.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MinimalTextPrimary
                            )

                            if (action.dosageOrMethod != null) {
                                Surface(
                                    color = CleanGreenLime.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = action.dosageOrMethod,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CleanGreenPrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = action.instruction,
                            fontSize = 12.sp,
                            color = MinimalTextSecondary,
                            lineHeight = 16.sp
                        )

                        if (action.safetyWarning != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = CriticalRed,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = action.safetyWarning,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CriticalRed
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IPMProtocolLibraryView(
    searchQuery: String,
    selectedCrop: String?,
    onSearchChange: (String) -> Unit,
    onCropSelect: (String?) -> Unit,
    onSelectRecommendation: (IPMProtocolRecommendation) -> Unit
) {
    val lang = LocalAppLanguage.current
    val crops = listOf("All", "Tomato", "Potato", "Chilli", "Rice / Paddy", "Maize", "Cucumber")

    val searchResults = remember(searchQuery, selectedCrop) {
        val base = IPMDecisionTreeData.searchRecommendations(searchQuery)
        if (selectedCrop == null || selectedCrop == "All") {
            base
        } else {
            base.filter { rec ->
                rec.targetCrops.any { it.contains(selectedCrop, ignoreCase = true) }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 32.dp)
    ) {
        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text(AppStrings.ipmSearchPlaceholder(lang), fontSize = 13.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = CleanGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchChange("") },
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search", modifier = Modifier.size(16.dp))
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CleanGreenPrimary,
                    unfocusedBorderColor = MinimalBorder,
                    focusedContainerColor = MinimalSurface,
                    unfocusedContainerColor = MinimalSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_ipm_search")
            )
        }

        // Crop Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(crops) { crop ->
                    val isSelected = (selectedCrop == null && crop == "All") || selectedCrop == crop
                    val localizedCrop = if (crop == "All") AppStrings.allFilter(lang) else AppStrings.localizeCropName(crop, lang)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCropSelect(if (crop == "All") null else crop) },
                        label = {
                            Text(
                                text = localizedCrop,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CleanGreenPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = MinimalSurface,
                            labelColor = MinimalTextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) CleanGreenPrimary else MinimalBorder,
                            enabled = true,
                            selected = isSelected
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("chip_ipm_crop_${crop.take(4).lowercase()}")
                    )
                }
            }
        }

        // Results Count
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = AppStrings.ipmProtocolsAvailable(searchResults.size, lang),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MinimalTextSecondary
                )
            }
        }

        // List of Recommendations or Empty State
        if (searchResults.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MinimalSurface),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, MinimalBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("card_no_ipm_search_results")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = MinimalTextMuted,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = AppStrings.noMatchingIpmProtocols(lang),
                            fontWeight = FontWeight.Bold,
                            color = MinimalTextPrimary,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = AppStrings.tryBroaderIpmSearch(lang),
                            color = MinimalTextSecondary,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = {
                                onSearchChange("")
                                onCropSelect(null)
                            },
                            modifier = Modifier.minimumInteractiveComponentSize().testTag("btn_clear_ipm_filters")
                        ) {
                            Text(AppStrings.resetSearchFilterBtn(lang), fontSize = 12.sp)
                        }
                    }
                }
            }
        } else {
            items(searchResults) { rec ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MinimalSurface),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, MinimalBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectRecommendation(rec) }
                    .testTag("card_ipm_rec_${rec.diseaseId}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = rec.diseaseName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MinimalTextPrimary
                        )

                        SeverityBadge(severity = rec.severityLevel)
                    }

                    Text(
                        text = rec.scientificName,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = MinimalTextSecondary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = rec.keySymptomsSummary,
                        fontSize = 12.sp,
                        color = MinimalTextMuted,
                        maxLines = 2,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${AppStrings.cropsLabel(lang)}: ${rec.targetCrops.map { AppStrings.localizeCropName(it, lang) }.joinToString(", ")}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = CleanGreenPrimary
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = AppStrings.view4TierPlan(lang),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CleanGreenPrimary
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = CleanGreenPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
}