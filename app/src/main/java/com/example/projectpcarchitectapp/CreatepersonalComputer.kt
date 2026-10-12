package com.example.projectpcarchitectapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpcarchitectapp.ui.theme.ProjectPCArchitectAPPTheme

@Composable
fun CreatePC(
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

        Spacer(modifier = Modifier.height(12.dp))

        MenuButton(text = "Back", onClick = onBack)
    }
}

@Preview(showBackground = true)
@Composable
private fun CreatePCPreview() {
    ProjectPCArchitectAPPTheme {
        CreatePC(onBack = {})
    }
}