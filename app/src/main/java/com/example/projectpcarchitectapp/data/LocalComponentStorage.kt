package com.example.projectpcarchitectapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object LocalComponentStorage {
    private const val FILE_NAME = "custom_components.json"
    private val gson = Gson()

    fun saveComponent(context: Context, component: Component) {
        val currentList = getCustomComponents(context).toMutableList()
        currentList.add(component)
        try {
            val file = File(context.filesDir, FILE_NAME)
            val json = gson.toJson(currentList)
            file.writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCustomComponents(context: Context?): List<Component> {
        if (context == null) return emptyList()
        try {
            val file = File(context.filesDir, FILE_NAME)
            if (!file.exists()) return emptyList()
            val json = file.readText()
            val type = object : TypeToken<List<Component>>() {}.type
            return gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
}
