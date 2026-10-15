package com.example.projectpcarchitectapp

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectpcarchitectapp.ui.theme.ProjectPCArchitectAPPTheme

@Composable
fun addComponent(
    addCPU: () -> Unit,
    addMotherBoard: () -> Unit,
    addRam: () -> Unit,
    addGPU: () -> Unit,
    addPSU: () -> Unit,
    addCASE: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(textAlign = TextAlign.Center,
            text = "Build New PC Configuration",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // TODO: component pickers (CPU, GPU, RAM, ...) go here
        var selectedCategory by remember {
            mutableStateOf<String?>(null)
        }

        if (selectedCategory == null) {
            IconMenuButton(
                text = "Select CPU",
                iconRes = R.drawable.memory_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
                onClick = {
                    selectedCategory = "CPU"
                }
            )
            IconMenuButton(
                text = "Select Motherboard",
                iconRes = R.drawable.memory_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
                onClick = {
                    selectedCategory = "MB"
                }
            )
            IconMenuButton(
                text = "Select RAM",
                iconRes = R.drawable.memory_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
                onClick = {
                    selectedCategory = "RAM"
                }
            )
            IconMenuButton(
                text = "Select GPU",
                iconRes = R.drawable.memory_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
                onClick = {
                    selectedCategory = "GPU"
                }
            )
            IconMenuButton(
                text = "Select PSU",
                iconRes = R.drawable.memory_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
                onClick = {
                    selectedCategory = "PSU"
                }
            )
            IconMenuButton(
                text = "Select Case",
                iconRes = R.drawable.memory_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
                onClick = {
                    selectedCategory = "Case"
                }
            )
        } else {
            ComponentListScreen(
                category = selectedCategory!!,
                viewModel = viewModel(),
                onItemSelected = { item ->
                    // Save the selected item to your PC build.
                    // Then return to the component menu.
                    selectedCategory = null
                }
            )
        }
     /*   IconMenuButton(text = "Select MotherBoard", iconRes = R.drawable.ic_launcher_background, onClick = {selectedCategory = "Motherboard"})
        IconMenuButton(text = "Select RAM", iconRes = R.drawable.memory_alt_24dp_1f1f1f_fill0_wght400_grad0_opsz24,{selectedCategory = "RAM"})
        IconMenuButton(text = "Select GPU", iconRes = R.drawable.desktop_windows_24dp_1f1f1f_fill0_wght400_grad0_opsz24,{selectedCategory = "GPU"})
        IconMenuButton(text = "Select PSU", iconRes = R.drawable.hard_drive_24dp_1f1f1f_fill0_wght400_grad0_opsz24,{selectedCategory = "PSU"})
        IconMenuButton(text = "select Case",iconRes = R.drawable.hard_disk_24dp_1f1f1f_fill0_wght400_grad0_opsz24,{selectedCategory = "Case"})
        Spacer(modifier = Modifier.height(12.dp))
*/
        MenuButton(text = "Back", onClick = onBack)
    }
}

@Preview(showBackground = true)
@Composable
private fun AddComponentPreview() {
    ProjectPCArchitectAPPTheme {
        CreatePC(onBack = {}, addMotherBoard = {}, addRam = {}, addGPU = {}
            , addPSU = {}, addCASE = {}, addCPU = {})
    }
}


