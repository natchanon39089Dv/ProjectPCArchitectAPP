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
    val components = _components.asStateFlow()

    fun loadComponents(category: String) {
        db.collection("components")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { result ->
                _components.value = result.documents.mapNotNull {
                    it.toObject(PCComponentItem::class.java)
                        ?.copy(id = it.id)
                }
            }
    }
}
