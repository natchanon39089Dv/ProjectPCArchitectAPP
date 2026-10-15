package com.example.projectpcarchitectapp

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpcarchitectapp.ui.theme.ProjectPCArchitectAPPTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainScreen(
    onNavigateToSupport: () -> Unit,
    onNavigateToLoadCF: () -> Unit,
    onBuildNewPC: () -> Unit,
    onNavigateToAddComponent: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: "PC Architect User"
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Welcome back,",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = currentUserEmail,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                TextButton(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        onLogout()
                    }
                ) {
                    Text(
                        text = "Logout",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // App Banner Image
        Image(
            painter = painterResource(R.drawable.pc_architect),
            contentDescription = "PC Architect Logo",
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "PC Architect Studio",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Design, Build & Cost Your Dream PC",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Menu Buttons with Icons & Better Look
        DashboardMenuButton(
            title = "Build New PC Configuration",
            subtitle = "Select CPU, GPU, RAM, Motherboard & more",
            iconRes = R.drawable.icons8_video_card_50,
            onClick = onBuildNewPC
        )

        Spacer(modifier = Modifier.height(12.dp))

        DashboardMenuButton(
            title = "Load Configuration",
            subtitle = "View, load, or manage saved PC builds",
            iconRes = R.drawable.hard_drive_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
            onClick = onNavigateToLoadCF
        )

        Spacer(modifier = Modifier.height(12.dp))

        DashboardMenuButton(
            title = "Add New Component",
            subtitle = "Add custom CPU, Motherboard, RAM, etc.",
            iconRes = R.drawable.memory_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
            onClick = onNavigateToAddComponent
        )

        Spacer(modifier = Modifier.height(12.dp))

        DashboardMenuButton(
            title = "Support This Project",
            subtitle = "Donate and support development",
            iconRes = R.drawable.hard_disk_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
            onClick = onNavigateToSupport
        )
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun DashboardMenuButton(
    title: String,
    subtitle: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
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
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    ProjectPCArchitectAPPTheme {
        MainScreen(
            onBuildNewPC = {},
            onNavigateToSupport = {},
            onNavigateToLoadCF = {},
            onNavigateToAddComponent = {},
            onLogout = {}
        )
    }
}
