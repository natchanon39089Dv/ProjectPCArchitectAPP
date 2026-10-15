package com.example.projectpcarchitectapp.ui.theme

import androidx.lifecycle.ViewModel
import com.example.projectpcarchitectapp.data.Component
import com.example.projectpcarchitectapp.data.SavedConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PCBuildState(
    val buildName: String = "My Custom PC",
    val cpu: Component? = null,
    val motherboard: Component? = null,
    val ram: Component? = null,
    val gpu: Component? = null,
    val psu: Component? = null,
    val pcCase: Component? = null
) {
    val totalPrice: Double
        get() = (cpu?.price ?: 0.0) +
                (motherboard?.price ?: 0.0) +
                (ram?.price ?: 0.0) +
                (gpu?.price ?: 0.0) +
                (psu?.price ?: 0.0) +
                (pcCase?.price ?: 0.0)
}

enum class PCComponentType {
    CPU, MOTHERBOARD, RAM, GPU, PSU, CASE
}

class PCBuilderViewModel(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _buildState = MutableStateFlow(PCBuildState())
    val buildState: StateFlow<PCBuildState> = _buildState.asStateFlow()

    fun updateBuildName(name: String) {
        _buildState.value = _buildState.value.copy(buildName = name)
    }

    fun selectCpu(component: Component) {
        _buildState.value = _buildState.value.copy(cpu = component)
    }

    fun selectMotherboard(component: Component) {
        _buildState.value = _buildState.value.copy(motherboard = component)
    }

    fun selectRam(component: Component) {
        _buildState.value = _buildState.value.copy(ram = component)
    }

    fun selectGpu(component: Component) {
        _buildState.value = _buildState.value.copy(gpu = component)
    }

    fun selectPsu(component: Component) {
        _buildState.value = _buildState.value.copy(psu = component)
    }

    fun selectCase(component: Component) {
        _buildState.value = _buildState.value.copy(pcCase = component)
    }

    fun deselectComponent(type: PCComponentType) {
        _buildState.value = when (type) {
            PCComponentType.CPU -> _buildState.value.copy(cpu = null)
            PCComponentType.MOTHERBOARD -> _buildState.value.copy(motherboard = null)
            PCComponentType.RAM -> _buildState.value.copy(ram = null)
            PCComponentType.GPU -> _buildState.value.copy(gpu = null)
            PCComponentType.PSU -> _buildState.value.copy(psu = null)
            PCComponentType.CASE -> _buildState.value.copy(pcCase = null)
        }
    }

    fun loadConfig(config: SavedConfig) {
        _buildState.value = PCBuildState(
            buildName = config.buildName,
            cpu = if (config.cpuName.isNotEmpty()) Component(name = config.cpuName) else null,
            motherboard = if (config.motherboardName.isNotEmpty()) Component(name = config.motherboardName) else null,
            ram = if (config.ramName.isNotEmpty()) Component(name = config.ramName) else null,
            gpu = if (config.gpuName.isNotEmpty()) Component(name = config.gpuName) else null,
            psu = if (config.psuName.isNotEmpty()) Component(name = config.psuName) else null,
            pcCase = if (config.caseName.isNotEmpty()) Component(name = config.caseName) else null
        )
    }

    fun clearBuild() {
        _buildState.value = PCBuildState()
    }

    fun saveConfiguration(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentState = _buildState.value
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        val savedConfig = SavedConfig(
            buildName = currentState.buildName.ifEmpty { "Untitled Build" },
            totalPrice = currentState.totalPrice,
            dateCreated = currentDate,
            cpuName = currentState.cpu?.name ?: "",
            gpuName = currentState.gpu?.name ?: "",
            ramName = currentState.ram?.name ?: "",
            motherboardName = currentState.motherboard?.name ?: "",
            psuName = currentState.psu?.name ?: "",
            caseName = currentState.pcCase?.name ?: "",
            authorId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        )

        db.collection("saved_configs")
            .add(savedConfig)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.localizedMessage ?: "Failed to save configuration")
            }
    }
}
