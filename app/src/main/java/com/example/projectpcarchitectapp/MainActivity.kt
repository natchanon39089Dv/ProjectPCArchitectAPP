package com.example.projectpcarchitectapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projectpcarchitectapp.ui.AddComponentScreen
import com.example.projectpcarchitectapp.ui.LoadConfigScreen
import com.example.projectpcarchitectapp.ui.SelectComponentScreen
import com.example.projectpcarchitectapp.ui.theme.PCBuilderViewModel
import com.example.projectpcarchitectapp.ui.theme.ProjectPCArchitectAPPTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Force sign out on every app launch so user must login or register every time they enter the app
        FirebaseAuth.getInstance().signOut()
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val builderViewModel: PCBuilderViewModel = viewModel()
            val startDestination = "login"

            ProjectPCArchitectAPPTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(
                                onLoginClick = { email, password, onError ->
                                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                        .addOnSuccessListener {
                                            navController.navigate("MainScreen") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            onError(e.localizedMessage ?: "Login failed")
                                        }
                                },
                                onNavigateToRegister = {
                                    navController.navigate("register")
                                }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                onRegisterClick = { email, password, onError ->
                                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                                        .addOnSuccessListener {
                                            navController.navigate("MainScreen") {
                                                popUpTo("register") { inclusive = true }
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            onError(e.localizedMessage ?: "Registration failed")
                                        }
                                },
                                onNavigateToLogin = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable("MainScreen") {
                            MainScreen(
                                onBuildNewPC = { navController.navigate("CreateNewPC") },
                                onNavigateToSupport = { navController.navigate("donate") },
                                onNavigateToLoadCF = { navController.navigate("loadConfig") },
                                onNavigateToAddComponent = { navController.navigate("addComponent") },
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("CreateNewPC") {
                            CreatePC(
                                builderViewModel = builderViewModel,
                                onBack = { navController.popBackStack() },
                                addCPU = { navController.navigate("addCPU") },
                                addRam = { navController.navigate("addRam") },
                                addPSU = { navController.navigate("addPSU") },
                                addGPU = { navController.navigate("addGPU") },
                                addMotherBoard = { navController.navigate("addMotherBoard") },
                                addCASE = { navController.navigate("addCase") }
                            )
                        }
                        composable("donate") {
                            Support(onBack = { navController.popBackStack() })
                        }
                        composable("loadConfig") {
                            LoadConfigScreen(
                                onLoadConfig = { config ->
                                    builderViewModel.loadConfig(config)
                                    navController.navigate("CreateNewPC") {
                                        popUpTo("MainScreen")
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("addComponent") {
                            AddComponentScreen(
                                onBack = { navController.popBackStack() }
                            )
                        }

                        // Component Selection Routes with Drawables & Shared ViewModel for Socket Filtering
                        composable("addCPU") {
                            SelectComponentScreen(
                                categoryTitle = "Select CPU",
                                categoryKey = "CPU",
                                iconRes = R.drawable.memory_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
                                builderViewModel = builderViewModel,
                                onComponentSelected = { selectedComponent ->
                                    builderViewModel.selectCpu(selectedComponent)
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() },
                                onAddCustomComponent = { navController.navigate("addComponent") }
                            )
                        }
                        composable("addMotherBoard") {
                            SelectComponentScreen(
                                categoryTitle = "Select Motherboard",
                                categoryKey = "Motherboard",
                                iconRes = R.drawable.ic_launcher_background,
                                builderViewModel = builderViewModel,
                                onComponentSelected = { selectedComponent ->
                                    builderViewModel.selectMotherboard(selectedComponent)
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() },
                                onAddCustomComponent = { navController.navigate("addComponent") }
                            )
                        }
                        composable("addRam") {
                            SelectComponentScreen(
                                categoryTitle = "Select RAM",
                                categoryKey = "RAM",
                                iconRes = R.drawable.memory_alt_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
                                builderViewModel = builderViewModel,
                                onComponentSelected = { selectedComponent ->
                                    builderViewModel.selectRam(selectedComponent)
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() },
                                onAddCustomComponent = { navController.navigate("addComponent") }
                            )
                        }
                        composable("addGPU") {
                            SelectComponentScreen(
                                categoryTitle = "Select Graphics Card",
                                categoryKey = "GPU",
                                iconRes = R.drawable.desktop_windows_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
                                builderViewModel = builderViewModel,
                                onComponentSelected = { selectedComponent ->
                                    builderViewModel.selectGpu(selectedComponent)
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() },
                                onAddCustomComponent = { navController.navigate("addComponent") }
                            )
                        }
                        composable("addPSU") {
                            SelectComponentScreen(
                                categoryTitle = "Select Power Supply",
                                categoryKey = "PSU",
                                iconRes = R.drawable.hard_drive_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
                                builderViewModel = builderViewModel,
                                onComponentSelected = { selectedComponent ->
                                    builderViewModel.selectPsu(selectedComponent)
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() },
                                onAddCustomComponent = { navController.navigate("addComponent") }
                            )
                        }
                        composable("addCase") {
                            SelectComponentScreen(
                                categoryTitle = "Select PC Case",
                                categoryKey = "Case",
                                iconRes = R.drawable.hard_disk_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
                                builderViewModel = builderViewModel,
                                onComponentSelected = { selectedComponent ->
                                    builderViewModel.selectCase(selectedComponent)
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() },
                                onAddCustomComponent = { navController.navigate("addComponent") }
                            )
                        }
                    }
                }
            }
        }
    }
}
