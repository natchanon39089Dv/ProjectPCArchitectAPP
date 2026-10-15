package com.example.projectpcarchitectapp.data


data class Component(
    val id: String = "",
    val category: String = "", // e.g., "CPU", "RAM", "GPU", "PSU", "Motherboard", "Case"
    val name: String = "",
    val manufacturer: String = "",
    val price: Double = 0.0,
    val specs: String = "",
    val authorId: String = ""
)
data class SavedConfig(
    val id: String = "",
    val buildName: String = "Untitled Build",
    val totalPrice: Double = 0.0,
    val dateCreated: String = "",
    val cpuName: String = "",
    val gpuName: String = "",
    val ramName: String = "",
    val motherboardName: String = "",
    val psuName: String = "",
    val caseName: String = "",
    val authorId: String = ""
)
