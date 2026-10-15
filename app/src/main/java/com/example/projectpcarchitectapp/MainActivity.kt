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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.projectpcarchitectapp.ui.theme.ProjectPCArchitectAPPTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ProjectPCArchitectAPPTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "MainScreen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("MainScreen") {
                            MainScreen(
                                onBuildNewPC = { navController.navigate("CreateNewPC") },
                                onNavigateToSupport = { navController.navigate("donate") },
                                onNavigateToLoadCF = {navController.navigate(route = "loadConfig")}
                            )
                        }
                        composable("CreateNewPC") {
                            CreatePC(onBack = { navController.popBackStack() },
                                    addCPU = {navController.navigate("addCPU")},
                                    addRam = {navController.navigate(route="addRam")},
                                    addPSU = {navController.navigate(route="addPSU")},
                                    addGPU = {navController.navigate(route="addGPU")},
                                    addMotherBoard = {navController.navigate(route="addMotherboard")},
                                    addCASE = {navController.navigate(route="addCase")})
                        }
                        composable(route = "donate") {
                            Support(onBack = { navController.popBackStack() })
                        }
                        composable("loadConfig"){
                            loadCf(onBack = { navController.popBackStack() })
                        }
                        composable(route = "addCPU"){
                            CreatePC(
                                onBack = { navController.popBackStack() },
                                addCPU = {},
                                addMotherBoard = {},
                                addRam = {},
                                addGPU = {},
                                addPSU = {},
                                addCASE = {}

                            )
                        }
                        composable(route = "addRam"){

                        }
                        composable(route = "addPSU"){

                        }
                        composable(route = "addGPU"){

                        }
                        composable(route = "addMotherBoard") {

                        }
                        composable(route = "addCase"){

                        }
                        }
                    }
                }
            }
        }
    }
