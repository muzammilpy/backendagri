package com.agrishield.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agrishield.app.data.model.*
import com.agrishield.app.domain.DiagnosisState
import com.agrishield.app.domain.DiagnosisViewModel
import com.agrishield.app.ui.theme.*

@Composable
fun ResultScreen(vm: DiagnosisViewModel, onBack: () -> Unit) {
    val diagState by vm.diagState.collectAsState()
    val language  by vm.language.collectAsState()

    val result = (diagState as? DiagnosisState.Success)?.result

    Column(
        Modifier.fillMaxSize().background(DarkBg)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(Modifier.fillMaxWidth().background(Color(0xFF0A2419)).padding(16.dp, 12.dp),
            Arrangement.SpaceBetween, Alignment.CenterVertically) {
            IconButton(onBack) { Icon(Icons.Default.ArrowBack, null, tint = Emerald500) }
            Text("Diagnosis Result", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.width(48.dp))
        }

        if (result == null) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("No result yet. Capture a leaf photo first.", color = Color.White.copy(0.6f))
            }
            return@Column
        }

        val top  = result.topPrediction
        val lang = language.code
        val displayName = when (lang) {
            "mr" -> top.marathi
            "hi" -> top.hindi
            else -> top.displayName
        }
        val sevColor = when (top.severity) {
            Severity.CRITICAL -> DangerRed
            Severity.HIGH     -> WarnAmber
            Severity.MODERATE -> Color(0xFFF59E0B)
            Severity.LOW      -> Emerald500
        }

        Spacer(Modifier.height(16.dp))

        // Severity banner
        Surface(Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            RoundedCornerShape(16.dp), sevColor.copy(0.15f)) {
            Row(Modifier.padding(16.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(displayName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    Text(top.label, color = Color.White.copy(0.6f), fontSize = 11.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                }
                Surface(shape = RoundedCornerShape(20.dp), color = sevColor.copy(0.2f)) {
                    Text(top.severity.name, color = sevColor, fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp, modifier = Modifier.padding(12.dp, 6.dp))
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Confidence dial card
        Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            RoundedCornerShape(16.dp), CardDefaults.cardColors(Color(0xFF0C2C20))) {
            Column(Modifier.padding(20.dp)) {
                Text("ML Confidence Score", color = Emerald300, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                // Confidence bar
                val conf = top.confidence / 100f
                LinearProgressIndicator(
                    progress    = { conf },
                    modifier    = Modifier.fillMaxWidth().height(10.dp),
                    color       = sevColor,
                    trackColor  = Color.White.copy(0.1f)
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("${"%.1f".format(top.confidence)}%", color = sevColor, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text(result.confidenceBand, color = Color.White.copy(0.6f), fontSize = 11.sp,
                        modifier = Modifier.align(Alignment.Bottom))
                }
                Spacer(Modifier.height(4.dp))
                Text("Source: ${result.modelSource}", color = Color.White.copy(0.4f), fontSize = 10.sp)
                Text("Inference: ${top.inferenceMseconds}ms on-device", color = Color.White.copy(0.4f), fontSize = 10.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        // All class probabilities
        Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            RoundedCornerShape(16.dp), CardDefaults.cardColors(Color(0xFF0C2C20))) {
            Column(Modifier.padding(20.dp)) {
                Text("All Class Probabilities", color = Emerald300, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                result.allPredictions.forEach { pred ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Text(pred.displayName, color = Color.White.copy(0.85f), fontSize = 12.sp, modifier = Modifier.weight(1f))
                        Text("${"%.1f".format(pred.confidence)}%",
                            color = if (pred.rank == 1) Emerald500 else Color.White.copy(0.5f),
                            fontSize = 12.sp, fontWeight = if (pred.rank == 1) FontWeight.ExtraBold else FontWeight.Normal)
                    }
                    LinearProgressIndicator(
                        progress   = { pred.confidence / 100f },
                        modifier   = Modifier.fillMaxWidth().height(4.dp).padding(top = 2.dp),
                        color      = if (pred.rank == 1) Emerald500 else Color.White.copy(0.2f),
                        trackColor = Color.Transparent
                    )
                    Spacer(Modifier.height(4.dp))
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Immediate action
        Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            RoundedCornerShape(16.dp), CardDefaults.cardColors(Color(0xFF0C3822))) {
            Column(Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, null, tint = WarnAmber, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Immediate Action", color = Emerald300, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                }
                Spacer(Modifier.height(10.dp))
                Text(top.immediateAction, color = Color.White.copy(0.9f), fontSize = 13.sp, lineHeight = 20.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        // Biological control
        if (top.biological.isNotBlank()) {
            Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                RoundedCornerShape(16.dp), CardDefaults.cardColors(Color(0xFF0C2C20))) {
                Column(Modifier.padding(20.dp)) {
                    Text("🍃 Biological / Organic Control", color = Emerald300, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(top.biological, color = Color.White.copy(0.85f), fontSize = 13.sp, lineHeight = 20.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // Chemical control
        if (top.chemical.isNotBlank()) {
            Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                RoundedCornerShape(16.dp), CardDefaults.cardColors(Color(0xFF2A0C0C))) {
                Column(Modifier.padding(20.dp)) {
                    Text("🧪 Chemical Control (PHI Mandatory)", color = Color(0xFFFFCDD2), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(top.chemical, color = Color(0xFFEF9A9A), fontSize = 13.sp, lineHeight = 20.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // Likely causes
        if (top.likelyCauses.isNotEmpty()) {
            Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                RoundedCornerShape(16.dp), CardDefaults.cardColors(Color(0xFF0C2C20))) {
                Column(Modifier.padding(20.dp)) {
                    Text("Likely Causes", color = Emerald300, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    top.likelyCauses.forEach {
                        Row(Modifier.padding(vertical = 2.dp)) {
                            Text("• ", color = Emerald500, fontWeight = FontWeight.Bold)
                            Text(it, color = Color.White.copy(0.85f), fontSize = 13.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // Weather context
        result.weatherRisk?.let { wr ->
            Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                RoundedCornerShape(16.dp), CardDefaults.cardColors(Color(0xFF0A1F2E))) {
                Column(Modifier.padding(20.dp)) {
                    Text("⛅ Current Weather Context", color = Color(0xFF93C5FD), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                        WeatherStat("Temp", "${wr.tempMax}°C")
                        WeatherStat("RH", "${wr.humidityMax}%")
                        WeatherStat("Rain", "${wr.rainMm}mm")
                        WeatherStat("Fungal", wr.fungalRisk.name)
                    }
                    Spacer(Modifier.height(10.dp))
                    val advisory = when (lang) {
                        "mr" -> wr.advisoryMr.ifBlank { wr.advisory }
                        "hi" -> wr.advisoryHi.ifBlank { wr.advisory }
                        else -> wr.advisory
                    }
                    Text(advisory, color = Color(0xFF93C5FD).copy(0.85f), fontSize = 12.sp, lineHeight = 18.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // GPS
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            Arrangement.Center, Alignment.CenterVertically) {
            Icon(Icons.Default.Place, null, tint = Emerald500, modifier = Modifier.size(14.dp))
            Text("  Farm GPS: ${"%.4f".format(result.farmLat)}, ${"%.4f".format(result.farmLon)}",
                color = Color.White.copy(0.4f), fontSize = 11.sp)
        }

        Spacer(Modifier.height(12.dp))

        // Voice advisory button
        Button(
            onClick  = { vm.speak("${top.displayName}. ${top.immediateAction}") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors   = ButtonDefaults.buttonColors(Color(0xFF1D4ED8))
        ) {
            Icon(Icons.Default.PlayArrow, null)
            Spacer(Modifier.width(8.dp))
            Text("🔊 Voice Advisory")
        }

        // Feedback
        Spacer(Modifier.height(8.dp))
        var showFeedback by remember { mutableStateOf(false) }
        OutlinedButton(
            onClick  = { showFeedback = true },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors   = ButtonDefaults.outlinedButtonColors(contentColor = Emerald500)
        ) {
            Icon(Icons.Default.Edit, null)
            Spacer(Modifier.width(8.dp))
            Text("Submit Treatment Outcome")
        }

        if (showFeedback) {
            FeedbackDialog(
                onDismiss = { showFeedback = false },
                onSubmit  = { outcome, notes ->
                    // Use first diagnosis id from history as approximation
                    vm.saveFeedback("latest", outcome, notes)
                    showFeedback = false
                }
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun WeatherStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color(0xFF93C5FD), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color(0xFF93C5FD).copy(0.6f), fontSize = 10.sp)
    }
}

@Composable
private fun FeedbackDialog(onDismiss: () -> Unit, onSubmit: (String, String) -> Unit) {
    var notes by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title   = { Text("Treatment Outcome") },
        text    = {
            Column {
                Text("Did the recommended treatment work?", fontSize = 13.sp)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(notes, { notes = it }, label = { Text("Notes (optional)") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Column {
                Button({ onSubmit("SUCCESS", notes) }, Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(Emerald700)) { Text("✅ Worked") }
                Spacer(Modifier.height(4.dp))
                OutlinedButton({ onSubmit("PARTIAL", notes) }, Modifier.fillMaxWidth()) { Text("⚠️ Partial") }
                Spacer(Modifier.height(4.dp))
                OutlinedButton({ onSubmit("FAILED", notes) }, Modifier.fillMaxWidth()) { Text("❌ Did Not Work") }
            }
        }
    )
}
