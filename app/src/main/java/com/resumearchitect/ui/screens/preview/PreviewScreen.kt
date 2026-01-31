package com.resumearchitect.ui.screens.preview

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.resumearchitect.ui.components.*
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    resumeId: String,
    navController: NavController,
    viewModel: PreviewViewModel = hiltViewModel()
) {
    val resume by viewModel.resume.collectAsState()
    val previewState by viewModel.previewState.collectAsState()
    val exportState by viewModel.exportState.collectAsState()
    
    // Show export success snackbar
    LaunchedEffect(exportState) {
        when (exportState) {
            is ExportState.Success -> {
                // Success message will be shown in UI
            }
            is ExportState.Error -> {
                // Error message will be shown in UI
            }
            else -> {}
        }
    }
    
    Scaffold(
        topBar = {
            PreviewTopBar(
                title = resume?.title ?: "Resume Preview",
                onBackClick = { navController.navigateUp() },
                onTemplateClick = {
                    // TODO: Navigate to template selection
                }
            )
        },
        bottomBar = {
            PreviewBottomBar(
                onExportClick = { viewModel.exportResume() },
                onShareClick = { viewModel.shareResume() },
                onEmailClick = { viewModel.emailResume() },
                isExporting = exportState is ExportState.Exporting
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = previewState) {
                is PreviewState.Loading -> {
                    LoadingView()
                }
                is PreviewState.Success -> {
                    PdfPreviewView(
                        pdfFile = state.file,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is PreviewState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = { viewModel.generatePreview() }
                    )
                }
            }
            
            // Export status overlay
            when (val state = exportState) {
                is ExportState.Success -> {
                    SuccessSnackbar(
                        message = "Resume saved to Downloads!",
                        fileName = state.file.name,
                        onDismiss = { viewModel.resetExportState() }
                    )
                }
                is ExportState.Error -> {
                    ErrorSnackbar(
                        message = state.message,
                        onDismiss = { viewModel.resetExportState() }
                    )
                }
                else -> {}
            }
        }
    }
}

@Composable
fun PreviewTopBar(
    title: String,
    onBackClick: () -> Unit,
    onTemplateClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        cornerRadius = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                GlassIconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            GlassButton(
                onClick = onTemplateClick,
                variant = GlassButtonVariant.OUTLINED
            ) {
                Icon(
                    Icons.Default.Palette,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Change Template")
            }
        }
    }
}

@Composable
fun PreviewBottomBar(
    onExportClick: () -> Unit,
    onShareClick: () -> Unit,
    onEmailClick: () -> Unit,
    isExporting: Boolean
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Export button
            GlassButton(
                onClick = onExportClick,
                variant = GlassButtonVariant.PRIMARY,
                modifier = Modifier.weight(1f),
                isLoading = isExporting
            ) {
                if (!isExporting) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(if (isExporting) "Exporting..." else "Export PDF")
            }
            
            // Share button
            GlassIconButton(
                onClick = onShareClick,
                enabled = !isExporting
            ) {
                Icon(Icons.Default.Share, contentDescription = "Share")
            }
            
            // Email button
            GlassIconButton(
                onClick = onEmailClick,
                enabled = !isExporting
            ) {
                Icon(Icons.Default.Email, contentDescription = "Email")
            }
        }
    }
}

@Composable
fun PdfPreviewView(
    pdfFile: File,
    modifier: Modifier = Modifier
) {
    // For now, show a placeholder
    // In production, integrate a PDF viewer library like AndroidPdfViewer
    // or use a WebView with Google Docs Viewer
    
    GlassCard(
        modifier = modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Description,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "PDF Preview",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = pdfFile.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Generated successfully!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "PDF viewer integration coming soon",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "Generating preview...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                Icons.Default.Error,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.error
            )
            
            Text(
                text = "Preview Failed",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            GlassButton(
                onClick = onRetry,
                variant = GlassButtonVariant.PRIMARY
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Try Again")
            }
        }
    }
}

@Composable
fun SuccessSnackbar(
    message: String,
    fileName: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = fileName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                
                GlassIconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Dismiss")
                }
            }
        }
    }
}

@Composable
fun ErrorSnackbar(
    message: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                GlassIconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Dismiss")
                }
            }
        }
    }
}
