package com.example.projectpcarchitectapp

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
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
        IconMenuButton(text =  "Select CPU",iconRes = R.drawable.memory_24dp_1f1f1f_fill0_wght400_grad0_opsz24 , onClick = onBack)
        IconMenuButton(text = "Select MotherBoard", iconRes = R.drawable.ic_launcher_background,onBack)
        IconMenuButton(text = "Select RAM", iconRes = R.drawable.memory_alt_24dp_1f1f1f_fill0_wght400_grad0_opsz24,onBack)
        IconMenuButton(text = "Select GPU", iconRes = R.drawable.desktop_windows_24dp_1f1f1f_fill0_wght400_grad0_opsz24,onBack)
        IconMenuButton(text = "Select PSU", iconRes = R.drawable.hard_drive_24dp_1f1f1f_fill0_wght400_grad0_opsz24,onBack)
        IconMenuButton(text = "select Case",iconRes = R.drawable.hard_disk_24dp_1f1f1f_fill0_wght400_grad0_opsz24,onBack)
        Spacer(modifier = Modifier.height(12.dp))

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
