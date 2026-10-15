package com.example.projectpcarchitectapp

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectpcarchitectapp.ui.theme.PCBuilderViewModel
import com.example.projectpcarchitectapp.ui.theme.PCComponentType
import com.example.projectpcarchitectapp.ui.theme.ProjectPCArchitectAPPTheme
import java.util.Locale

@Composable
fun CreatePC(
    addCPU: () -> Unit,
    addMotherBoard: () -> Unit,
    addRam: () -> Unit,
    addGPU: () -> Unit,
    addPSU: () -> Unit,
    addCASE: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    builderViewModel: PCBuilderViewModel = viewModel()
) {
    val buildState by builderViewModel.buildState.collectAsState()
    var saveMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = "Build New PC Configuration",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Build Name input
        OutlinedTextField(
            value = buildState.buildName,
            onValueChange = { builderViewModel.updateBuildName(it) },
            label = { Text("Build Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Selection Menu Items with current selection preview & deselect button
        ComponentSelectionRow(
            title = "CPU",
            selectedName = buildState.cpu?.name,
            selectedPrice = buildState.cpu?.price,
            iconRes = R.drawable.memory_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
            onClick = addCPU,
            onDeselect = { builderViewModel.deselectComponent(PCComponentType.CPU) }
        )

        ComponentSelectionRow(
            title = "Motherboard",
            selectedName = buildState.motherboard?.name,
            selectedPrice = buildState.motherboard?.price,
            iconRes = R.drawable.icons8_motherboard_96,
            onClick = addMotherBoard,
            onDeselect = { builderViewModel.deselectComponent(PCComponentType.MOTHERBOARD) }
        )

        ComponentSelectionRow(
            title = "RAM",
            selectedName = buildState.ram?.name,
            selectedPrice = buildState.ram?.price,
            iconRes = R.drawable.memory_alt_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
            onClick = addRam,
            onDeselect = { builderViewModel.deselectComponent(PCComponentType.RAM) }
        )

        ComponentSelectionRow(
            title = "Graphics Card (GPU)",
            selectedName = buildState.gpu?.name,
            selectedPrice = buildState.gpu?.price,
            iconRes = R.drawable.icons8_video_card_50,
            onClick = addGPU,
            onDeselect = { builderViewModel.deselectComponent(PCComponentType.GPU) }
        )

        ComponentSelectionRow(
            title = "Power Supply (PSU)",
            selectedName = buildState.psu?.name,
            selectedPrice = buildState.psu?.price,
            iconRes = R.drawable.hard_drive_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
            onClick = addPSU,
            onDeselect = { builderViewModel.deselectComponent(PCComponentType.PSU) }
        )

        ComponentSelectionRow(
            title = "PC Case",
            selectedName = buildState.pcCase?.name,
            selectedPrice = buildState.pcCase?.price,
            iconRes = R.drawable.hard_disk_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
            onClick = addCASE,
            onDeselect = { builderViewModel.deselectComponent(PCComponentType.CASE) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Update Screen Block / Summary Block
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Configuration Summary",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                SummaryLine("CPU", buildState.cpu?.name, buildState.cpu?.price)
                SummaryLine("Motherboard", buildState.motherboard?.name, buildState.motherboard?.price)
                SummaryLine("RAM", buildState.ram?.name, buildState.ram?.price)
                SummaryLine("GPU", buildState.gpu?.name, buildState.gpu?.price)
                SummaryLine("PSU", buildState.psu?.name, buildState.psu?.price)
                SummaryLine("Case", buildState.pcCase?.name, buildState.pcCase?.price)

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Price:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "฿${String.format(Locale.US, "%.2f", buildState.totalPrice)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Save Button
        Button(
            onClick = {
                builderViewModel.saveConfiguration(
                    onSuccess = { saveMessage = "Configuration saved successfully to Firebase!" },
                    onError = { err -> saveMessage = "Error: $err" }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Save Configuration to Firebase", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        if (saveMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = saveMessage!!,
                color = if (saveMessage!!.startsWith("Error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        MenuButton(text = "Back", onClick = onBack)
    }
}

@Composable
fun ComponentSelectionRow(
    title: String,
    selectedName: String?,
    selectedPrice: Double?,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    onDeselect: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = selectedName ?: "Tap to select $title",
                    fontSize = 15.sp,
                    fontWeight = if (selectedName != null) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedName != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
                )
            }
            if (selectedPrice != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "฿$selectedPrice",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onDeselect,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Text(
                            text = "✕",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryLine(label: String, name: String?, price: Double?) {
    if (name != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "$label: $name", fontSize = 13.sp, modifier = Modifier.weight(1f))
            if (price != null) {
                Text(text = "฿$price", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreatePCPreview() {
    ProjectPCArchitectAPPTheme {
        CreatePC(
            onBack = {},
            addMotherBoard = {},
            addRam = {},
            addGPU = {},
            addPSU = {},
            addCASE = {},
            addCPU = {}
        )
    }
}
