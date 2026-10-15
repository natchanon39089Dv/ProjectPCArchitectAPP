package com.example.projectpcarchitectapp

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PCBuilderViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _components =
        MutableStateFlow<List<PCComponentItem>>(emptyList())
    val components: StateFlow<List<PCComponentItem>> =
        _components.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadComponents(category: String) {
        _isLoading.value = true
        _error.value = null

        db.collection("components")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { result ->
                _components.value = result.documents.mapNotNull {
                    it.toObject(PCComponentItem::class.java)
                        ?.copy(id = it.id)
                }
                _isLoading.value = false
            }
            .addOnFailureListener { exception ->
                _error.value = exception.localizedMessage
                    ?: "Failed to load components"
                _isLoading.value = false
            }
    }
}
