package com.example.projectpcarchitectapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectpcarchitectapp.data.Component
import com.example.projectpcarchitectapp.data.LocalComponentStorage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddComponentScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("CPU") }
    var manufacturer by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var customSpecs by remember { mutableStateOf("") }

    var selectedSocket by remember { mutableStateOf("AM5") }
    var selectedDdr by remember { mutableStateOf("DDR5") }
    var message by remember { mutableStateOf<String?>(null) }

    val categories = listOf("CPU", "Motherboard", "RAM", "GPU", "PSU", "Case")
    val sockets = listOf("AM5", "AM4", "LGA1700", "LGA1200")
    val ddrTypes = listOf("DDR5", "DDR4")

    val scrollState = rememberScrollState()
    val db = FirebaseFirestore.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Component", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add Custom PC Component",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selector
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "Category: $category", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        categories.forEach { cat ->
                            Button(
                                onClick = { category = cat },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (category == cat) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                                ),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(2.dp)
                            ) {
                                Text(text = cat, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            // Socket Selector (if CPU or Motherboard)
            if (category == "CPU" || category == "Motherboard") {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "CPU Socket: $selectedSocket", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            sockets.forEach { sock ->
                                Button(
                                    onClick = { selectedSocket = sock },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedSocket == sock) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                                    ),
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(4.dp)
                                ) {
                                    Text(text = sock, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }

            // DDR Selector (if Motherboard or RAM)
            if (category == "Motherboard" || category == "RAM") {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "Memory Type: $selectedDdr", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ddrTypes.forEach { ddr ->
                                Button(
                                    onClick = { selectedDdr = ddr },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedDdr == ddr) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = ddr, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Component Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = manufacturer,
                onValueChange = { manufacturer = it },
                label = { Text("Manufacturer / Brand (e.g. ASUS, Intel)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price (฿)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = customSpecs,
                onValueChange = { customSpecs = it },
                label = { Text("Additional Specs (e.g. 16 Cores, 5.0 GHz)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (name.isBlank() || price.isBlank()) {
                        message = "Please enter component name and price."
                        return@Button
                    }

                    val specsBuilder = StringBuilder()
                    if (category == "CPU" || category == "Motherboard") {
                        specsBuilder.append("Socket: $selectedSocket, ")
                    }
                    if (category == "Motherboard" || category == "RAM") {
                        specsBuilder.append("Memory: $selectedDdr, ")
                    }
                    if (customSpecs.isNotBlank()) {
                        specsBuilder.append(customSpecs)
                    }

                    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    val newComponent = Component(
                        id = "custom_${System.currentTimeMillis()}",
                        category = category,
                        name = name,
                        manufacturer = manufacturer,
                        price = price.toDoubleOrNull() ?: 0.0,
                        specs = specsBuilder.toString().trim().removeSuffix(","),
                        authorId = currentUserId
                    )

                    LocalComponentStorage.saveComponent(context, newComponent)

                    val componentData = hashMapOf(
                        "category" to category,
                        "name" to name,
                        "manufacturer" to manufacturer,
                        "price" to newComponent.price,
                        "specs" to newComponent.specs,
                        "authorId" to currentUserId
                    )
                    db.collection("components")
                        .add(componentData)
                        .addOnSuccessListener {
                            message = "Component added successfully with compatibility specs!"
                            name = ""
                            manufacturer = ""
                            price = ""
                            customSpecs = ""
                        }
                        .addOnFailureListener { e ->
                            message = "Saved locally! (Firebase sync notice: ${e.localizedMessage})"
                            name = ""
                            manufacturer = ""
                            price = ""
                            customSpecs = ""
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Save Component", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            if (message != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = message!!,
                    color = if (message!!.startsWith("Error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
