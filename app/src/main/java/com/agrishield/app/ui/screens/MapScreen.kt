package com.agrishield.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.agrishield.app.data.model.Severity
import com.agrishield.app.domain.DiagnosisViewModel
import com.agrishield.app.domain.GISRepository
import com.agrishield.app.ui.theme.*

@Composable
fun MapScreen(vm: DiagnosisViewModel) {
    val language by vm.language.collectAsState()
    val lang = language.code

    var selectedFilter by remember { mutableStateOf("ALL") }
    var selectedDistrict by remember { mutableStateOf<com.agrishield.app.data.model.DistrictHotspot?>(null) }

    val allDistricts = GISRepository.maharashtraDistricts
    val filtered = when (selectedFilter) {
        "CRITICAL" -> allDistricts.filter { it.outbreakRisk == Severity.CRITICAL }
        "HIGH"     -> allDistricts.filter { it.outbreakRisk == Severity.HIGH }
        "Cotton"   -> GISRepository.getByCrop("Cotton")
        "Rice"     -> GISRepository.getByCrop("Rice")
        else       -> allDistricts
    }

    val critical = allDistricts.count { it.outbreakRisk == Severity.CRITICAL }
    val high     = allDistricts.count { it.outbreakRisk == Severity.HIGH }

    Column(Modifier.fillMaxSize().background(DarkBg)) {
        // Header
        Row(Modifier.fillMaxWidth().background(Color(0xFF0A2419)).padding(16.dp, 12.dp),
            Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text("🗺️ Maharashtra GIS Map", color = Emerald500, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Text("${allDistricts.size} districts", color = Emerald300.copy(0.7f), fontSize = 12.sp)
        }

        // Stats bar
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), Arrangement.spacedBy(8.dp)) {
            StatChip("🔴 Critical", "$critical", DangerRed)
            StatChip("🟠 High", "$high", WarnAmber)
            StatChip("📍 Total", "${allDistricts.size}", Emerald500)
        }

        // Filter chips
        LazyRow(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val filters = listOf("ALL","CRITICAL","HIGH","Cotton","Rice","Grapes")
            items(filters) { f ->
                FilterChip(
                    selected = selectedFilter == f,
                    onClick  = { selectedFilter = f },
                    label    = { Text(f, fontSize = 12.sp) }
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        // District list
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered, key = { it.name }) { district ->
                DistrictCard(district, lang) { selectedDistrict = district }
            }
        }
    }

    // Detail bottom sheet
    selectedDistrict?.let { d ->
        AlertDialog(
            onDismissRequest = { selectedDistrict = null },
            title   = { Text("${d.name} — ${d.division}", fontWeight = FontWeight.ExtraBold) },
            text    = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    DetailRow("Risk", d.outbreakRisk.name)
                    DetailRow("Threat", d.primaryThreat)
                    DetailRow("Crops", d.majorCrops.joinToString(", "))
                    DetailRow("Soil", d.soilType)
                    DetailRow("GPS", "${"%.4f".format(d.lat)}, ${"%.4f".format(d.lon)}")
                    if (d.trapCount > 0) DetailRow("Trap Catch", "${d.trapCount} moths/night")
                    DetailRow("NDVI", d.ndvi.toString())
                    Spacer(Modifier.height(4.dp))
                    Text(if (lang == "mr") d.advisoryMr.ifBlank { d.advisory } else d.advisory,
                        fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.8f))
                }
            },
            confirmButton = {
                TextButton({ selectedDistrict = null }) { Text("Close") }
            }
        )
    }
}

@Composable
private fun DistrictCard(d: com.agrishield.app.data.model.DistrictHotspot, lang: String, onClick: () -> Unit) {
    val riskColor = when (d.outbreakRisk) {
        Severity.CRITICAL -> DangerRed
        Severity.HIGH     -> WarnAmber
        Severity.MODERATE -> Color(0xFFF59E0B)
        Severity.LOW      -> Emerald500
    }
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick),
        RoundedCornerShape(14.dp), CardDefaults.cardColors(Color(0xFF0C2C20))) {
        Row(Modifier.padding(14.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(50), color = riskColor.copy(0.2f)) {
                        Text(d.outbreakRisk.name, color = riskColor, fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(8.dp, 3.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(d.name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                }
                Spacer(Modifier.height(4.dp))
                Text(d.primaryThreat, color = riskColor.copy(0.85f), fontSize = 12.sp)
                Text(d.majorCrops.joinToString(" · "), color = Color.White.copy(0.5f), fontSize = 11.sp)
                if (d.trapCount > 0) {
                    Text("Trap: ${d.trapCount} moths/night", color = WarnAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Icon(Icons.Default.ArrowForward, null, tint = Color.White.copy(0.4f))
        }
    }
}

@Composable
private fun StatChip(label: String, value: String, color: Color) {
    Surface(shape = RoundedCornerShape(10.dp), color = color.copy(0.15f)) {
        Row(Modifier.padding(10.dp, 6.dp), Arrangement.spacedBy(4.dp), Alignment.CenterVertically) {
            Text(label, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(value, color = color, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth()) {
        Text("$label: ", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.width(80.dp))
        Text(value, fontSize = 12.sp)
    }
}
