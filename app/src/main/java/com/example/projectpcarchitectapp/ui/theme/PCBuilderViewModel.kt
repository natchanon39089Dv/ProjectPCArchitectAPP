package com.example.projectpcarchitectapp.ui.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class PCComponent {
    CPU,
    MOTHERBOARD,
    RAM,
    GPU,
    PSU,
    CASE
}

data class PCBuildState(
    val cpu: String? = null,
    val motherboard: String? = null,
    val ram: String? = null,
    val gpu: String? = null,
    val psu: String? = null,
    val pcCase: String? = null
)

class PCBuilderViewModel : ViewModel() {

    private val _buildState = MutableStateFlow(PCBuildState())
    val buildState: StateFlow<PCBuildState> =
        _buildState.asStateFlow()

    fun selectComponent(
        component: PCComponent,
        name: String
    ) {
        _buildState.value = when (component) {
            PCComponent.CPU ->
                _buildState.value.copy(cpu = name)

            PCComponent.MOTHERBOARD ->
                _buildState.value.copy(motherboard = name)

            PCComponent.RAM ->
                _buildState.value.copy(ram = name)

            PCComponent.GPU ->
                _buildState.value.copy(gpu = name)

            PCComponent.PSU ->
                _buildState.value.copy(psu = name)

            PCComponent.CASE ->
                _buildState.value.copy(pcCase = name)
        }
    }

    fun clearBuild() {
        _buildState.value = PCBuildState()
    }
}
