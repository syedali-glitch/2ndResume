package com.resumearchitect.ui.screens.builder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.resumearchitect.data.models.PersonalInfo
import com.resumearchitect.ui.components.*

@Composable
fun ContactInfoScreen(viewModel: BuilderViewModel) {
    val personalInfo by viewModel.personalInfo.collectAsState()
    
    // Local state for form fields
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var professionalTitle by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var linkedIn by remember { mutableStateOf("") }
    var portfolio by remember { mutableStateOf("") }
    var github by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    
    // Initialize from database
    LaunchedEffect(personalInfo) {
        personalInfo?.let { info ->
            fullName = info.fullName
            email = info.email
            phone = info.phone
            professionalTitle = info.professionalTitle
            city = info.city
            state = info.state
            linkedIn = info.linkedIn
            portfolio = info.portfolio
            github = info.github
            summary = info.summary
        }
    }
    
    // Auto-save debounced
    LaunchedEffect(fullName, email, phone, professionalTitle, city, state, 
                   linkedIn, portfolio, github, summary) {
        kotlinx.coroutines.delay(500) // Debounce
        
        personalInfo?.let { info ->
            viewModel.updatePersonalInfo(
                info.copy(
                    fullName = fullName,
                    email = email,
                    phone = phone,
                    professionalTitle = professionalTitle,
                    city = city,
                    state = state,
                    linkedIn = linkedIn,
                    portfolio = portfolio,
                    github = github,
                    summary = summary
                )
            )
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "Contact Information",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Text(
            text = "Let employers know how to reach you",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Basic Information Card
        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Basic Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                GlassTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = "Full Name *",
                    placeholder = "John Doe",
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
                
                GlassTextField(
                    value = professionalTitle,
                    onValueChange = { professionalTitle = it },
                    label = "Professional Title",
                    placeholder = "Senior Software Engineer",
                    leadingIcon = {
                        Icon(
                            Icons.Default.Work,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
                
                GlassTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address *",
                    placeholder = "john.doe@example.com",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
                
                GlassTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Phone Number",
                    placeholder = "(555) 123-4567",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlassTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = "City",
                        placeholder = "New York",
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                    
                    GlassTextField(
                        value = state,
                        onValueChange = { state = it },
                        label = "State",
                        placeholder = "NY",
                        modifier = Modifier.weight(0.5f)
                    )
                }
            }
        }
        
        // Online Presence Card
        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Online Presence",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                GlassTextField(
                    value = linkedIn,
                    onValueChange = { linkedIn = it },
                    label = "LinkedIn",
                    placeholder = "linkedin.com/in/johndoe",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Link,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
                
                GlassTextField(
                    value = portfolio,
                    onValueChange = { portfolio = it },
                    label = "Portfolio/Website",
                    placeholder = "www.johndoe.com",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Language,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
                
                GlassTextField(
                    value = github,
                    onValueChange = { github = it },
                    label = "GitHub",
                    placeholder = "github.com/johndoe",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Code,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }
        
        // Professional Summary Card
        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Professional Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "A brief overview of your professional background and career goals",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                GlassTextField(
                    value = summary,
                    onValueChange = { summary = it },
                    placeholder = "Experienced software engineer with expertise in mobile development...",
                    singleLine = false,
                    maxLines = 6,
                    minLines = 4
                )
                
                Text(
                    text = "${summary.length} characters",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
        
        // Spacer for bottom navigation
        Spacer(modifier = Modifier.height(32.dp))
    }
}
