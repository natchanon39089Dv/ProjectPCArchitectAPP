package com.example.projectpcarchitectapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpcarchitectapp.data.Component
import com.example.projectpcarchitectapp.data.ComponentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed interface ComponentUiState {
    object Loading : ComponentUiState
    data class Success(val components: List<Component>) : ComponentUiState
    data class Error(val message: String) : ComponentUiState
}

class ComponentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ComponentRepository(application)

    private val _uiState = MutableStateFlow<ComponentUiState>(ComponentUiState.Loading)
    val uiState: StateFlow<ComponentUiState> = _uiState.asStateFlow()

    fun loadComponents(category: String) {
        viewModelScope.launch {
            _uiState.value = ComponentUiState.Loading
            repository.getComponentsByCategory(category)
                .catch { e ->
                    _uiState.value = ComponentUiState.Error(e.localizedMessage ?: "An error occurred")
                }
                .collect { list ->
                    _uiState.value = ComponentUiState.Success(list)
                }
        }
    }
}
