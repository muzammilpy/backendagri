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
import com.agrishield.app.data.model.Severity
import com.agrishield.app.domain.DiagnosisViewModel
import com.agrishield.app.domain.LocationState
import com.agrishield.app.ui.theme.*

@Composable
fun WeatherScreen(vm: DiagnosisViewModel) {
    val weather       by vm.weather.collectAsState()
    val locationState by vm.locationState.collectAsState()
    val language      by vm.language.collectAsState()
    val lang = language.code

    Column(Modifier.fillMaxSize().background(DarkBg)) {
        // Header
        Row(Modifier.fillMaxWidth().background(Color(0xFF0A2419)).padding(16.dp, 12.dp),
            Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Column {
                Text("🌦️ Microclimate Risk Engine", color = Emerald500, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Text(
                    when (locationState) {
                        is LocationState.Available -> "📍 ${(locationState as LocationState.Available).name}"
                        else -> "📡 Locating..."
                    }, color = Emerald300, fontSize = 11.sp
                )
            }
            IconButton({ vm.fetchWeather() }) {
                Icon(Icons.Default.Refresh, null, tint = Emerald500)
            }
        }

        if (weather == null) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Emerald500)
                    Spacer(Modifier.height(12.dp))
                    Text("Fetching Open-Meteo forecast for your GPS location...",
                        color = Color.White.copy(0.6f), fontSize = 13.sp)
                    Text("(Works offline with fallback model)", color = Emerald300.copy(0.5f), fontSize = 11.sp)
                }
            }
            return@Column
        }

        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                // Today summary card
                val today = weather!!.days.firstOrNull()
                if (today != null) {
                    val bigRisk = if (today.fungalRisk.ordinal >= today.pestRisk.ordinal) today.fungalRisk else today.pestRisk
                    val bigColor = when (bigRisk) {
                        Severity.CRITICAL -> DangerRed
                        Severity.HIGH     -> WarnAmber
                        Severity.MODERATE -> Color(0xFFF59E0B)
                        Severity.LOW      -> Emerald500
                    }
                    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(bigColor.copy(0.12f))) {
                        Column(Modifier.padding(20.dp)) {
                            Text("TODAY'S FARM RISK", color = bigColor, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                                WeatherVal("🌡️", "${today.tempMax}°C", "Max Temp")
                                WeatherVal("💧", "${today.humidityMax}%", "Max RH")
                                WeatherVal("🌧️", "${today.rainMm}mm", "Rain")
                                WeatherVal("💨", "${today.windKmh}km/h", "Wind")
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                                RiskPill("Fungal", today.fungalRisk)
                                RiskPill("Pest", today.pestRisk)
                            }
                            Spacer(Modifier.height(10.dp))
                            val adv = when (lang) {
                                "mr" -> today.advisoryMr.ifBlank { today.advisory }
                                "hi" -> today.advisoryHi.ifBlank { today.advisory }
                                else -> today.advisory
                            }
                            Text(adv, color = Color.White.copy(0.9f), fontSize = 13.sp, lineHeight = 20.sp)
                        }
                    }
                }
            }

            item { Text("7-DAY FORECAST", color = Emerald300, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(top = 4.dp)) }

            items(weather!!.days) { day ->
                Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(Color(0xFF0C2C20))) {
                    Column(Modifier.padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                            Text(day.condition, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                RiskPill("F", day.fungalRisk, compact = true)
                                RiskPill("P", day.pestRisk,   compact = true)
                            }
                        }
                        Spacer(Modifier.height(6.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("🌡️ ${day.tempMax}°/${day.tempMin}°C", color = Color.White.copy(0.8f), fontSize = 12.sp)
                            Text("💧 ${day.humidityMax}%", color = Color.White.copy(0.8f), fontSize = 12.sp)
                            Text("🌧️ ${day.rainMm}mm", color = Color.White.copy(0.8f), fontSize = 12.sp)
                        }
                        Spacer(Modifier.height(8.dp))
                        val adv = when (lang) {
                            "mr" -> day.advisoryMr.ifBlank { day.advisory }
                            "hi" -> day.advisoryHi.ifBlank { day.advisory }
                            else -> day.advisory
                        }
                        Text(adv, color = Color.White.copy(0.7f), fontSize = 11.sp, lineHeight = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherVal(icon: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 18.sp)
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
        Text(label, color = Color.White.copy(0.5f), fontSize = 10.sp)
    }
}

@Composable
private fun RiskPill(label: String, sev: Severity, compact: Boolean = false) {
    val color = when (sev) {
        Severity.CRITICAL -> DangerRed
        Severity.HIGH     -> WarnAmber
        Severity.MODERATE -> Color(0xFFF59E0B)
        Severity.LOW      -> Emerald500
    }
    Surface(shape = RoundedCornerShape(20.dp), color = color.copy(0.2f)) {
        Text(
            if (compact) "$label:${sev.name[0]}" else "$label: ${sev.name}",
            color = color, fontSize = if (compact) 10.sp else 11.sp,
            fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(8.dp, 4.dp)
        )
    }
}
