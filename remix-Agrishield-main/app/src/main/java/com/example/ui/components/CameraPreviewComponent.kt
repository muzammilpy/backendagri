package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.ui.theme.*
import java.io.File
import java.util.concurrent.Executors

/**
 * Composable CameraX Preview component for capturing crop leaf photographs
 * for AI symptom and pest diagnosis.
 */
@Composable
fun CameraPreviewComponent(
    onImageCaptured: (Uri) -> Unit,
    onClose: () -> Unit,
    onOpenGallery: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var permissionDeniedExplanation by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            permissionDeniedExplanation = true
        }
    }

    if (!hasCameraPermission) {
        CameraPermissionRequestCard(
            onRequestPermission = {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            },
            onOpenGallery = onOpenGallery,
            onClose = onClose,
            isDenied = permissionDeniedExplanation,
            modifier = modifier
        )
        return
    }

    // Camera State
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    var isTorchOn by remember { mutableStateOf(false) }
    var isCapturing by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showFlashShutter by remember { mutableStateOf(false) }

    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    var cameraInfo by remember { mutableStateOf<CameraInfo?>(null) }
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("camera_preview_container")
    ) {
        // Live CameraX Preview View
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }

                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    try {
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.surfaceProvider = previewView.surfaceProvider
                        }

                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(lensFacing)
                            .build()

                        cameraProvider.unbindAll()
                        val camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )

                        cameraControl = camera.cameraControl
                        cameraInfo = camera.cameraInfo
                    } catch (exc: Exception) {
                        Log.e("CameraPreviewComponent", "Use case binding failed", exc)
                        errorMessage = "Camera initialization failed: ${exc.localizedMessage}"
                    }
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            update = { previewView ->
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    try {
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.surfaceProvider = previewView.surfaceProvider
                        }

                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(lensFacing)
                            .build()

                        cameraProvider.unbindAll()
                        val camera = cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )

                        cameraControl = camera.cameraControl
                        cameraInfo = camera.cameraInfo
                    } catch (exc: Exception) {
                        Log.e("CameraPreviewComponent", "Lens switch failed", exc)
                    }
                }, ContextCompat.getMainExecutor(context))
            },
            modifier = Modifier.fillMaxSize()
        )

        // Viewfinder Framing and Targeting Overlay
        CropLeafViewfinderOverlay(
            modifier = Modifier.fillMaxSize()
        )

        // Top Controls Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Close / Back button
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .size(44.dp)
                    .testTag("btn_camera_close")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Camera",
                    tint = Color.White
                )
            }

            // Top Status Badge
            Surface(
                color = ForestGreenPrimary.copy(alpha = 0.85f),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MintSoft.copy(alpha = 0.5f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        Icons.Default.Eco,
                        contentDescription = null,
                        tint = MintSoft,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "AI Leaf Scanner",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Torch / Flash Toggle
            IconButton(
                onClick = {
                    isTorchOn = !isTorchOn
                    cameraControl?.enableTorch(isTorchOn)
                },
                modifier = Modifier
                    .background(
                        if (isTorchOn) EarthAmber.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.5f),
                        CircleShape
                    )
                    .size(44.dp)
                    .testTag("btn_camera_torch")
            ) {
                Icon(
                    imageVector = if (isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                    contentDescription = "Toggle Flash",
                    tint = Color.White
                )
            }
        }

        // Error Banner if capture fails
        errorMessage?.let { error ->
            Surface(
                color = CriticalRedContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp, start = 20.dp, end = 20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = CriticalRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(error, color = CriticalRed, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    IconButton(onClick = { errorMessage = null }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = CriticalRed, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        // Bottom Capture Controls Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f), Color.Black.copy(alpha = 0.95f))
                    )
                )
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Guiding Leaf Instruction Text
            Text(
                text = "Align infected leaf blade within the target frame",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Hold steady 15–20 cm away for crisp lesion focus",
                color = MintSoft,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons Row (Gallery, Shutter Button, Switch Camera)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gallery shortcut button
                IconButton(
                    onClick = onOpenGallery,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        .size(50.dp)
                        .testTag("btn_camera_gallery_shortcut")
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Choose from Gallery",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Primary Shutter / Capture Button
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .padding(6.dp)
                        .clip(CircleShape)
                        .background(if (isCapturing) EarthAmber else LeafGreen)
                        .clickable(enabled = !isCapturing) {
                            isCapturing = true
                            showFlashShutter = true

                            val photoFile = File(
                                context.cacheDir,
                                "crop_leaf_${System.currentTimeMillis()}.jpg"
                            )
                            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                            imageCapture.takePicture(
                                outputOptions,
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                        isCapturing = false
                                        val savedUri = Uri.fromFile(photoFile)
                                        onImageCaptured(savedUri)
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        isCapturing = false
                                        Log.e("CameraPreviewComponent", "Photo capture failed", exception)
                                        errorMessage = "Capture failed: ${exception.message}"
                                    }
                                }
                            )
                        }
                        .testTag("btn_capture_shutter"),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCapturing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Capture Leaf",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // Flip Camera Lens (Back / Front)
                IconButton(
                    onClick = {
                        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                            CameraSelector.LENS_FACING_FRONT
                        } else {
                            CameraSelector.LENS_FACING_BACK
                        }
                    },
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        .size(50.dp)
                        .testTag("btn_camera_flip_lens")
                ) {
                    Icon(
                        imageVector = Icons.Default.FlipCameraAndroid,
                        contentDescription = "Flip Camera",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Shutter White Flash Animation
        AnimatedVisibility(
            visible = showFlashShutter,
            enter = fadeIn(animationSpec = tween(50)),
            exit = fadeOut(animationSpec = tween(250))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.8f))
            )
            LaunchedEffect(showFlashShutter) {
                kotlinx.coroutines.delay(100)
                showFlashShutter = false
            }
        }
    }
}

/**
 * Viewfinder overlay with corner reticles and real-time scanning sweep animation
 */
@Composable
fun CropLeafViewfinderOverlay(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "viewfinder_laser")
    val sweepProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sweep_progress"
    )

    Canvas(modifier = modifier) {
        val frameWidth = size.width * 0.78f
        val frameHeight = size.height * 0.44f
        val left = (size.width - frameWidth) / 2f
        val top = (size.height - frameHeight) / 2f - 40.dp.toPx()
        val right = left + frameWidth
        val bottom = top + frameHeight
        val cornerLength = 32.dp.toPx()
        val strokeWidth = 3.5.dp.toPx()

        // Dim background outside focus rectangle
        val overlayPath = Path().apply {
            fillType = androidx.compose.ui.graphics.PathFillType.EvenOdd
            addRect(Rect(0f, 0f, size.width, size.height))
            addRoundRect(
                androidx.compose.ui.geometry.RoundRect(
                    left = left,
                    top = top,
                    right = right,
                    bottom = bottom,
                    cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx())
                )
            )
        }
        drawPath(
            path = overlayPath,
            color = Color.Black.copy(alpha = 0.4f)
        )

        // Draw 4 corner reticles
        val cornerColor = MintSoft

        // Top Left
        drawLine(cornerColor, Offset(left, top + cornerLength), Offset(left, top), strokeWidth)
        drawLine(cornerColor, Offset(left, top), Offset(left + cornerLength, top), strokeWidth)

        // Top Right
        drawLine(cornerColor, Offset(right - cornerLength, top), Offset(right, top), strokeWidth)
        drawLine(cornerColor, Offset(right, top), Offset(right, top + cornerLength), strokeWidth)

        // Bottom Left
        drawLine(cornerColor, Offset(left, bottom - cornerLength), Offset(left, bottom), strokeWidth)
        drawLine(cornerColor, Offset(left, bottom), Offset(left + cornerLength, bottom), strokeWidth)

        // Bottom Right
        drawLine(cornerColor, Offset(right - cornerLength, bottom), Offset(right, bottom), strokeWidth)
        drawLine(cornerColor, Offset(right, bottom), Offset(right, bottom - cornerLength), strokeWidth)

        // Laser scan sweep beam inside the frame
        val laserY = top + (bottom - top) * sweepProgress
        drawLine(
            brush = Brush.horizontalGradient(
                listOf(
                    Color.Transparent,
                    LeafGreen.copy(alpha = 0.6f),
                    MintSoft,
                    LeafGreen.copy(alpha = 0.6f),
                    Color.Transparent
                )
            ),
            start = Offset(left + 8.dp.toPx(), laserY),
            end = Offset(right - 8.dp.toPx(), laserY),
            strokeWidth = 3.dp.toPx()
        )
    }
}

/**
 * Friendly Permission Request View when Camera access has not been granted
 */
@Composable
fun CameraPermissionRequestCard(
    onRequestPermission: () -> Unit,
    onOpenGallery: () -> Unit,
    onClose: () -> Unit,
    isDenied: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MinimalBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_camera_permission_request")
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Icon
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MintSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera Permission",
                        tint = ForestGreenPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Text(
                    text = "Camera Permission Required",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (isDenied) {
                        "Camera permission was not granted. Please allow camera access in settings or grant permission to photograph infected crop leaves directly."
                    } else {
                        "Enable camera access so you can capture crisp field photographs of diseased crop leaves and insect pests for instant Gemini AI IPM diagnosis."
                    },
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Primary Request Button
                Button(
                    onClick = onRequestPermission,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("btn_grant_camera_permission")
                ) {
                    Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Grant Camera Permission", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                // Secondary Gallery Button
                OutlinedButton(
                    onClick = onOpenGallery,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("btn_permission_open_gallery")
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Choose Photo from Gallery Instead", fontSize = 13.sp)
                }

                // Cancel button
                TextButton(
                    onClick = onClose,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text("Cancel", color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}
