package com.example.recipesapp.database

import androidx.room.TypeConverter
import com.example.recipesapp.models.AnalyzedInstruction
import com.example.recipesapp.models.ExtendedIngredient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//typeconvertors for items inserted into/retrieved from the database
class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun analyzedInstructionToString(analyzedInstruction: List<AnalyzedInstruction>): String {
        return gson.toJson(analyzedInstruction)
    }

    @TypeConverter
    fun stringToAnalyzedInstruction(data: String): List<AnalyzedInstruction> {
        val listType = object : TypeToken<List<AnalyzedInstruction>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun stringListToString(stringList: List<String>): String {
        return gson.toJson(stringList)
    }

    @TypeConverter
    fun stringToStringList(data: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun fromExtendedIngredient(extendedIngredients: List<ExtendedIngredient?>?): String? {
        val listType = object : TypeToken<List<ExtendedIngredient>>() {}.type
        return gson.toJson(extendedIngredients, listType)
    }

    @TypeConverter
    fun toExtendedIngredient(extendedIngredient: String?): List<ExtendedIngredient>? {
        val listType = object : TypeToken<List<ExtendedIngredient>>() {}.type
        return gson.fromJson(extendedIngredient, listType)
    }
}
