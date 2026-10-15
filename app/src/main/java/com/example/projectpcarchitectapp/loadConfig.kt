package com.example.projectpcarchitectapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectpcarchitectapp.data.SavedConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed interface LoadConfigUiState {
    object Loading : LoadConfigUiState
    data class Success(val configs: List<SavedConfig>) : LoadConfigUiState
    data class Error(val message: String) : LoadConfigUiState
}

class LoadConfigViewModel(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoadConfigUiState>(LoadConfigUiState.Loading)
    val uiState: StateFlow<LoadConfigUiState> = _uiState.asStateFlow()

    init {
        fetchSavedConfigs()
    }

    private fun fetchSavedConfigs() {
        viewModelScope.launch {
            _uiState.value = LoadConfigUiState.Loading

            val flow = kotlinx.coroutines.flow.callbackFlow {
                val listener = db.collection("saved_configs")
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            close(error)
                            return@addSnapshotListener
                        }
                        val list = snapshot?.documents?.mapNotNull { doc ->
                            doc.toObject(SavedConfig::class.java)?.copy(id = doc.id)
                        } ?: emptyList()
                        trySend(list)
                    }
                awaitClose { listener.remove() }
            }

            flow.catch { e ->
                _uiState.value = LoadConfigUiState.Error(e.localizedMessage ?: "Failed to load configs")
            }.collect { list ->
                _uiState.value = LoadConfigUiState.Success(list)
            }
        }
    }

    fun deleteConfig(configId: String) {
        if (configId.isNotEmpty()) {
            db.collection("saved_configs").document(configId).delete()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadConfigScreen(
    onLoadConfig: (SavedConfig) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoadConfigViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Load Configuration", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            when (val state = uiState) {
                is LoadConfigUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is LoadConfigUiState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is LoadConfigUiState.Success -> {
                    if (state.configs.isEmpty()) {
                        Text(
                            text = "No saved configurations found.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.configs, key = { it.id }) { config ->
                                ConfigCard(
                                    config = config,
                                    onLoad = { onLoadConfig(config) },
                                    onDelete = { viewModel.deleteConfig(config.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConfigCard(
    config: SavedConfig,
    onLoad: () -> Unit,
    onDelete: () -> Unit
) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val isAuthor = config.authorId.isEmpty() || config.authorId == currentUserId

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = config.buildName.ifEmpty { "Untitled Build" },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "฿${config.totalPrice}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (config.cpuName.isNotEmpty()) Text("CPU: ${config.cpuName}", fontSize = 13.sp)
            if (config.gpuName.isNotEmpty()) Text("GPU: ${config.gpuName}", fontSize = 13.sp)
            if (config.ramName.isNotEmpty()) Text("RAM: ${config.ramName}", fontSize = 13.sp)
            if (config.motherboardName.isNotEmpty()) Text("Motherboard: ${config.motherboardName}", fontSize = 13.sp)
            if (config.psuName.isNotEmpty()) Text("PSU: ${config.psuName}", fontSize = 13.sp)
            if (config.caseName.isNotEmpty()) Text("Case: ${config.caseName}", fontSize = 13.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isAuthor) {
                    Text(
                        text = "🔒 Read-only (Not Author)",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Row {
                    if (isAuthor) {
                        TextButton(onClick = onDelete) {
                            Text("Delete", color = MaterialTheme.colorScheme.error)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Button(onClick = onLoad) {
                        Text("Load")
                    }
                }
            }
        }
    }
}
