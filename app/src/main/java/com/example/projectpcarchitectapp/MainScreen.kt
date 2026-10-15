package com.example.projectpcarchitectapp

import android.view.Menu
import androidx.annotation.Nullable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpcarchitectapp.ui.theme.ProjectPCArchitectAPPTheme
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
@Composable
fun MainScreen(
    onNavigateToSupport: () -> Unit,
    onNavigateToLoadCF: () -> Unit,
    onBuildNewPC: () -> Unit,
    modifier: Modifier = Modifier,

) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.pc_architect),
            contentDescription = "PC Architect Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(40.dp))

        MenuButton(text = "Build New PC Configuration", onClick = onBuildNewPC)

        Spacer(modifier = Modifier.height(12.dp))
        MenuButton(text = "Load Configuration", onClick = onNavigateToLoadCF)

        Spacer(modifier = Modifier.height(12.dp))
        MenuButton(text = "Export Configuration") { /* TODO */ }

        Spacer(modifier = Modifier.height(12.dp))
        MenuButton(text = "Add New Component") { /* TODO */ }

        Spacer(modifier = Modifier.height(12.dp))
        MenuButton(text = "Support This Project", onClick = onNavigateToSupport)
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    ProjectPCArchitectAPPTheme {
        MainScreen(onBuildNewPC = {}, onNavigateToSupport = {}, onNavigateToLoadCF = {})


    }
}