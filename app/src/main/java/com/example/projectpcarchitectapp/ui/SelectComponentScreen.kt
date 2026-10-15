package com.example.projectpcarchitectapp.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectpcarchitectapp.R
import com.example.projectpcarchitectapp.data.Component
import com.example.projectpcarchitectapp.ui.theme.PCBuilderViewModel

fun extractSocket(component: Component?): String? {
    if (component == null) return null
    val text = "${component.name} ${component.specs}".uppercase()
    return when {
        text.contains("AM5") -> "AM5"
        text.contains("AM4") -> "AM4"
        text.contains("LGA1700") -> "LGA1700"
        text.contains("LGA1200") -> "LGA1200"
        else -> null
    }
}

fun extractDdrType(component: Component?): String? {
    if (component == null) return null
    val text = "${component.name} ${component.specs}".uppercase()
    return when {
        text.contains("DDR5") -> "DDR5"
        text.contains("DDR4") -> "DDR4"
        else -> null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectComponentScreen(
    categoryTitle: String,
    categoryKey: String,
    @DrawableRes iconRes: Int,
    onComponentSelected: (Component) -> Unit,
    onBack: () -> Unit,
    onAddCustomComponent: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ComponentViewModel = viewModel(),
    builderViewModel: PCBuilderViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(categoryKey) {
        viewModel.loadComponents(categoryKey)
    }

    val uiState by viewModel.uiState.collectAsState()
    val buildState by builderViewModel.buildState.collectAsState()

    val cpuSocket = extractSocket(buildState.cpu)
    val mbSocket = extractSocket(buildState.motherboard)
    val mbDdr = extractDdrType(buildState.motherboard)
    val ramDdr = extractDdrType(buildState.ram)

    val filterNotes = mutableListOf<String>()
    if (categoryKey.equals("Motherboard", true)) {
        if (cpuSocket != null) filterNotes.add("CPU Socket: $cpuSocket")
        if (ramDdr != null) filterNotes.add("RAM Type: $ramDdr")
    } else if (categoryKey.equals("CPU", true)) {
        if (mbSocket != null) filterNotes.add("Motherboard Socket: $mbSocket")
    } else if (categoryKey.equals("RAM", true)) {
        if (mbDdr != null) filterNotes.add("Motherboard Memory: $mbDdr")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = categoryTitle, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Image(
                            painter = painterResource(id = R.drawable.pc_architectet),
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onAddCustomComponent) {
                        Text("+ Add", fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            if (filterNotes.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = "Auto-filtered by compatibility: ${filterNotes.joinToString(", ")}",
                        modifier = Modifier.padding(10.dp),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Search ${categoryTitle.lowercase()}...") },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = "Search Icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = onAddCustomComponent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    text = "+ Add New $categoryTitle",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (val state = uiState) {
                is ComponentUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is ComponentUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: ${state.message}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                is ComponentUiState.Success -> {
                    val filteredList = state.components.filter { comp ->
                        val matchesSearch = comp.name.contains(searchQuery, ignoreCase = true) ||
                                comp.manufacturer.contains(searchQuery, ignoreCase = true)

                        var matchesCompatibility = true
                        when (categoryKey.lowercase()) {
                            "motherboard" -> {
                                if (cpuSocket != null) {
                                    val compSocket = extractSocket(comp)
                                    if (compSocket != null && compSocket != cpuSocket) matchesCompatibility = false
                                }
                                if (ramDdr != null) {
                                    val compDdr = extractDdrType(comp)
                                    if (compDdr != null && compDdr != ramDdr) matchesCompatibility = false
                                }
                            }
                            "cpu" -> {
                                if (mbSocket != null) {
                                    val compSocket = extractSocket(comp)
                                    if (compSocket != null && compSocket != mbSocket) matchesCompatibility = false
                                }
                            }
                            "ram" -> {
                                if (mbDdr != null) {
                                    val compDdr = extractDdrType(comp)
                                    if (compDdr != null && compDdr != mbDdr) matchesCompatibility = false
                                }
                            }
                        }
                        matchesSearch && matchesCompatibility
                    }

                    if (filteredList.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (filterNotes.isNotEmpty())
                                    "No compatible components found matching: ${filterNotes.joinToString(", ")}"
                                else
                                    "No components found for \"$categoryKey\"",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(filteredList, key = { it.id.ifEmpty { it.name } }) { component ->
                                ComponentItemCard(
                                    component = component,
                                    iconRes = iconRes,
                                    onClick = { onComponentSelected(component) }
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
fun ComponentItemCard(
    component: Component,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = component.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "฿${component.price}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = component.manufacturer,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )

                if (component.specs.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = component.specs,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
