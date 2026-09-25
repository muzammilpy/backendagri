package com.agrishield.app.ui.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.agrishield.app.data.model.AppLanguage
import com.agrishield.app.domain.DiagnosisState
import com.agrishield.app.domain.DiagnosisViewModel
import com.agrishield.app.domain.LocationState
import com.agrishield.app.ui.theme.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(vm: DiagnosisViewModel, onResultReady: () -> Unit) {
    val context       = LocalContext.current
    val diagState     by vm.diagState.collectAsState()
    val locationState by vm.locationState.collectAsState()
    val language      by vm.language.collectAsState()

    val cameraPermission   = rememberPermissionState(Manifest.permission.CAMERA)
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { vm.classify(it) } }

    LaunchedEffect(diagState) {
        if (diagState is DiagnosisState.Success) onResultReady()
    }
    LaunchedEffect(Unit) {
        if (!cameraPermission.status.isGranted)   cameraPermission.launchPermissionRequest()
        if (!locationPermission.status.isGranted) locationPermission.launchPermissionRequest()
    }

    Box(Modifier.fillMaxSize().background(DarkBg)) {
        Column(Modifier.fillMaxSize()) {
            // Header
            Row(
                Modifier.fillMaxWidth().background(Color(0xFF0A2419)).padding(16.dp, 12.dp),
                Arrangement.SpaceBetween, Alignment.CenterVertically
            ) {
                Column {
                    Text("🌱 AgriShield", color = Emerald500, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                    Text(
                        when (locationState) {
                            is LocationState.Available -> "📍 ${(locationState as LocationState.Available).name}"
                            is LocationState.Waiting   -> "📡 Detecting GPS..."
                            else                       -> "📍 Location unavailable"
                        }, color = Emerald300, fontSize = 11.sp
                    )
                }
                // Language picker
                var langExpanded by remember { mutableStateOf(false) }
                Box {
                    TextButton({ langExpanded = true }) {
                        Text(language.displayName, color = Emerald300, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.MoreVert, null, tint = Emerald300)
                    }
                    DropdownMenu(langExpanded, { langExpanded = false }) {
                        AppLanguage.values().forEach { l ->
                            DropdownMenuItem({ Text(l.displayName) }, { vm.setLanguage(l); langExpanded = false })
                        }
                    }
                }
            }

            // Camera
            Box(Modifier.weight(1f).fillMaxWidth()) {
                if (cameraPermission.status.isGranted) {
                    val lifecycleOwner = LocalLifecycleOwner.current
                    val executor: Executor = remember { Executors.newSingleThreadExecutor() }
                    var capture by remember { mutableStateOf<ImageCapture?>(null) }

                    AndroidView(factory = { ctx ->
                        val pv = PreviewView(ctx)
                        ProcessCameraProvider.getInstance(ctx).addListener({
                            val cp = ProcessCameraProvider.getInstance(ctx).get()
                            val preview = Preview.Builder().build().also { it.setSurfaceProvider(pv.surfaceProvider) }
                            val ic = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build()
                            capture = ic
                            cp.unbindAll()
                            cp.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, ic)
                        }, ContextCompat.getMainExecutor(ctx))
                        pv
                    }, Modifier.fillMaxSize())

                    // Guide box
                    Box(Modifier.fillMaxSize()) {
                        Box(Modifier.size(220.dp).align(Alignment.Center).border(2.dp, Emerald500.copy(alpha = 0.8f), RoundedCornerShape(16.dp)))
                        Text("Place affected leaf in frame", color = Color.White.copy(0.85f), fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.TopCenter).padding(top = 52.dp)
                                .background(Color.Black.copy(0.4f), RoundedCornerShape(8.dp))
                                .padding(12.dp, 4.dp))
                    }

                    // Shutter
                    Box(Modifier.fillMaxSize(), Alignment.BottomCenter) {
                        Box(Modifier.padding(bottom = 28.dp).size(72.dp).clip(CircleShape)
                            .background(Color.White).border(4.dp, Emerald500, CircleShape),
                            Alignment.Center) {
                            IconButton(onClick = {
                                val ic = capture ?: return@IconButton
                                val f = File(context.cacheDir, "cap_${System.currentTimeMillis()}.jpg")
                                ic.takePicture(ImageCapture.OutputFileOptions.Builder(f).build(), executor,
                                    object : ImageCapture.OnImageSavedCallback {
                                        override fun onImageSaved(o: ImageCapture.OutputFileResults) { vm.classify(Uri.fromFile(f)) }
                                        override fun onError(e: ImageCaptureException) {}
                                    })
                            }, Modifier.fillMaxSize()) {
                                Icon(Icons.Default.Add, null, tint = Emerald900, modifier = Modifier.size(36.dp))
                            }
                        }
                    }
                } else {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Add, null, tint = Emerald500, modifier = Modifier.size(64.dp))
                            Spacer(Modifier.height(16.dp))
                            Text("Camera permission needed", color = Color.White, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Button({ cameraPermission.launchPermissionRequest() }, colors = ButtonDefaults.buttonColors(Emerald700)) { Text("Grant Permission") }
                        }
                    }
                }

                // Classifying overlay
                if (diagState is DiagnosisState.Classifying) {
                    Box(Modifier.fillMaxSize().background(Color.Black.copy(0.65f)), Alignment.Center) {
                        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(Color(0xFF0C2C20))) {
                            Column(Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = Emerald500)
                                Spacer(Modifier.height(14.dp))
                                Text("🔬 Running TFLite ML...", color = Emerald300, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                Text("MobileNetV2 · Cotton Disease Classifier", color = Emerald300.copy(0.6f), fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            // Bottom bar
            Row(Modifier.fillMaxWidth().background(Color(0xFF0A2419)).padding(16.dp),
                Arrangement.SpaceEvenly, Alignment.CenterVertically) {
                OutlinedButton({ galleryLauncher.launch("image/*") }, Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Emerald500)) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.width(6.dp))
                    Text("Gallery")
                }
                Spacer(Modifier.width(12.dp))
                val (stxt, scol) = when (diagState) {
                    is DiagnosisState.LoadingModel -> "Loading model..." to Emerald300
                    is DiagnosisState.Classifying  -> "Running ML..."    to Emerald500
                    is DiagnosisState.Error        -> "Error"            to DangerRed
                    else                           -> "Ready ✓"          to Emerald500
                }
                Surface(Modifier.weight(1f), RoundedCornerShape(20.dp), scol.copy(0.15f)) {
                    Text(stxt, color = scol, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center, modifier = Modifier.padding(12.dp, 10.dp))
                }
            }
        }
    }
}
