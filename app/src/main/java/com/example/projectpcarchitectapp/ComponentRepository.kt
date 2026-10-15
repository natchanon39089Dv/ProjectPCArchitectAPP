package com.example.projectpcarchitectapp.data

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ComponentRepository(
    private val context: Context? = null,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val defaultLocalComponents = listOf(
        // --- CPU ---
        Component("cpu_adv_1", "CPU", "AMD Ryzen 7 7800X3D (Advice / JIB)", "AMD", 14900.0, "Socket: AM5, 8 Cores / 16 Threads / 5.0 GHz"),
        Component("cpu_adv_2", "CPU", "Intel Core i5-13400F (Advice)", "Intel", 6790.0, "Socket: LGA1700, 10 Cores / 4.6 GHz"),
        Component("cpu_adv_3", "CPU", "AMD Ryzen 5 5600 (JIB Best Seller)", "AMD", 4590.0, "Socket: AM4, 6 Cores / 12 Threads / 4.4 GHz"),
        Component("cpu_adv_4", "CPU", "Intel Core i7-14700K (Advice / JIB)", "Intel", 14500.0, "Socket: LGA1700, 20 Cores / 5.6 GHz"),
        Component("cpu_adv_5", "CPU", "AMD Ryzen 9 7950X3D", "AMD", 21500.0, "Socket: AM5, 16 Cores / 5.7 GHz"),

        // --- Motherboard ---
        Component("mb_adv_1", "Motherboard", "ASUS TUF GAMING B650M-PLUS WIFI", "ASUS", 5890.0, "Socket: AM5, Memory: DDR5, Micro-ATX"),
        Component("mb_adv_2", "Motherboard", "MSI PRO B760M-A WIFI", "MSI", 4990.0, "Socket: LGA1700, Memory: DDR5, Micro-ATX"),
        Component("mb_3", "Motherboard", "Gigabyte B550M AORUS ELITE", "Gigabyte", 3490.0, "Socket: AM4, Memory: DDR4, Micro-ATX"),
        Component("mb_4", "Motherboard", "ASUS ROG STRIX Z790-E GAMING WIFI", "ASUS", 13900.0, "Socket: LGA1700, Memory: DDR5, ATX"),

        // --- RAM ---
        Component("ram_adv_1", "RAM", "Kingston FURY Beast 32GB (16x2) 5600MHz", "Kingston", 3690.0, "Memory: DDR5, 5600MHz, RGB"),
        Component("ram_adv_2", "RAM", "Corsair Vengeance RGB 32GB 6000MHz", "Corsair", 4190.0, "Memory: DDR5, 6000MHz, CL36"),
        Component("ram_3", "RAM", "Lexar THOR DDR4 16GB (8x2) 3200MHz", "Lexar", 1390.0, "Memory: DDR4, 3200MHz"),
        Component("ram_4", "RAM", "G.SKILL Trident Z5 RGB 64GB 6400MHz", "G.SKILL", 7900.0, "Memory: DDR5, 6400MHz"),

        // --- GPU ---
        Component("gpu_adv_1", "GPU", "GALAX GeForce RTX 4060 1-Click OC 8GB", "GALAX", 10900.0, "8GB GDDR6 / DLSS 3"),
        Component("gpu_adv_2", "GPU", "ASUS TUF Gaming RTX 4070 Ti SUPER 16G", "ASUS", 29900.0, "16GB GDDR6X / Triple Fan"),
        Component("gpu_adv_3", "GPU", "MSI Radeon RX 7800 XT GAMING X 16G", "MSI", 19500.0, "16GB GDDR6 / PCIe 4.0"),
        Component("gpu_adv_4", "GPU", "PNY GeForce RTX 4090 XLR8 24GB", "PNY", 65900.0, "24GB GDDR6X / Extreme Performance"),
        Component("gpu_adv_5", "GPU", "ASUS Dual GeForce RTX 4060 Ti OC 8GB", "ASUS", 13900.0, "8GB GDDR6"),

        // --- PSU ---
        Component("psu_adv_1", "PSU", "THERMALTAKE Toughpower GF 750W Gold", "Thermaltake", 3290.0, "750W / Fully Modular"),
        Component("psu_2", "PSU", "CORSAIR RM850e 850W Gold ATX 3.0", "Corsair", 4590.0, "850W / PCIe 5.0 Ready"),
        Component("psu_3", "PSU", "COUGAR GEX 650W 80+ Gold", "Cougar", 2590.0, "650W / Compact Size"),
        Component("psu_4", "PSU", "SUPER FLOWER Leadex VII 1000W", "Super Flower", 5900.0, "1000W / Fully Modular"),

        // --- Case ---
        Component("case_adv_1", "Case", "NZXT H6 Flow RGB", "NZXT", 4390.0, "Mid Tower / Dual Chamber / Tempered Glass"),
        Component("case_adv_2", "Case", "CORSAIR 4000D Airflow", "Corsair", 2990.0, "Mid Tower / High Airflow Front Panel"),
        Component("case_adv_3", "Case", "LIAN LI O11 Dynamic EVO (Black)", "Lian Li", 5490.0, "Full Tower / Modular Dual Chamber"),
        Component("case_adv_4", "Case", "COUGAR Duoface RGB Black", "Cougar", 2190.0, "Mid Tower / Interchangeable Front Panels")
    )

    fun getComponentsByCategory(category: String): Flow<List<Component>> = callbackFlow {
        val customLocal = LocalComponentStorage.getCustomComponents(context)
        val allLocal = defaultLocalComponents + customLocal
        val filteredLocal = allLocal.filter { it.category.equals(category, ignoreCase = true) }
        trySend(filteredLocal)

        val listenerRegistration = db.collection("components")
            .whereEqualTo("category", category)
            .addSnapshotListener { snapshot, error ->
                if (error == null && snapshot != null) {
                    val firestoreItems = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Component::class.java)?.copy(id = doc.id)
                    }.filter { it.category.equals(category, ignoreCase = true) }

                    val combined = (filteredLocal + firestoreItems).distinctBy { it.id.ifEmpty { it.name } }
                    trySend(combined)
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}
