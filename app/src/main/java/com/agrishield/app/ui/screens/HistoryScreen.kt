package com.agrishield.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.agrishield.app.data.model.DiagnosisEntity
import com.agrishield.app.data.model.Severity
import com.agrishield.app.domain.DiagnosisViewModel
import com.agrishield.app.domain.SyncWorker
import com.agrishield.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(vm: DiagnosisViewModel) {
    val allDiagnoses by vm.allDiagnoses.collectAsState()
    val language     by vm.language.collectAsState()

    Column(Modifier.fillMaxSize().background(DarkBg)) {
        // Header
        Row(Modifier.fillMaxWidth().background(Color(0xFF0A2419)).padding(16.dp, 12.dp),
            Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Column {
                Text("📋 Diagnosis History", color = Emerald500, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                val unsynced = allDiagnoses.count { !it.isSyncedToServer }
                Text("${allDiagnoses.size} total · $unsynced pending sync", color = Emerald300, fontSize = 11.sp)
            }
            // Manual sync button
            val ctx = androidx.compose.ui.platform.LocalContext.current
            IconButton({
                SyncWorker.runNow(ctx)
            }) {
                Icon(Icons.Default.Share, null, tint = Emerald500)
            }
        }

        if (allDiagnoses.isEmpty()) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Add, null, tint = Emerald500.copy(0.4f), modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("No diagnoses yet.", color = Color.White.copy(0.5f), fontSize = 14.sp)
                    Text("Take a leaf photo to begin.", color = Color.White.copy(0.3f), fontSize = 12.sp)
                }
            }
            return@Column
        }

        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                // Summary stats
                val bySeverity = allDiagnoses.groupBy { it.severity }
                Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                    listOf("CRITICAL" to DangerRed, "HIGH" to WarnAmber, "LOW" to Emerald500).forEach { (sev, col) ->
                        val cnt = bySeverity[sev]?.size ?: 0
                        Surface(Modifier.weight(1f), RoundedCornerShape(10.dp), col.copy(0.15f)) {
                            Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$cnt", color = col, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                                Text(sev, color = col.copy(0.7f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            items(allDiagnoses, key = { it.id }) { entity ->
                HistoryCard(entity)
            }
        }
    }
}

@Composable
private fun HistoryCard(entity: DiagnosisEntity) {
    val sevColor = when (entity.severity) {
        "CRITICAL" -> DangerRed
        "HIGH"     -> WarnAmber
        "LOW"      -> Emerald500
        else       -> Color(0xFFF59E0B)
    }
    val date = remember(entity.createdAt) {
        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            .format(Date(entity.createdAt))
    }

    Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(Color(0xFF0C2C20))) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(20.dp), color = sevColor.copy(0.2f)) {
                    Text(entity.severity, color = sevColor, fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(8.dp, 3.dp))
                }
                Text(date, color = Color.White.copy(0.4f), fontSize = 10.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(entity.diagnosisLabel, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
            Text("Confidence: ${"%.1f".format(entity.confidence)}%", color = sevColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Place, null, tint = Emerald300, modifier = Modifier.size(12.dp))
                Text("  ${entity.districtName}", color = Emerald300, fontSize = 11.sp)
                Spacer(Modifier.width(12.dp))
                // Sync status
                val syncIcon = if (entity.isSyncedToServer) Icons.Default.Check else Icons.Default.Close
                val syncColor = if (entity.isSyncedToServer) Emerald500 else Color.White.copy(0.3f)
                Icon(syncIcon, null, tint = syncColor, modifier = Modifier.size(12.dp))
                Text(
                    if (entity.isSyncedToServer) "  Synced" else "  Pending sync",
                    color = syncColor, fontSize = 11.sp
                )
            }
            // Feedback badge
            entity.feedbackOutcome?.let { outcome ->
                Spacer(Modifier.height(4.dp))
                val (fbColor, fbText) = when (outcome) {
                    "SUCCESS" -> Emerald500 to "✅ Treatment worked"
                    "PARTIAL" -> WarnAmber  to "⚠️ Partial result"
                    else      -> DangerRed  to "❌ Did not work"
                }
                Text(fbText, color = fbColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
