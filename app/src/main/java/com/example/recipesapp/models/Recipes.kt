package com.example.recipesapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Recipes(
    @SerializedName(value = "recipes", alternate = ["results"])
    val recipes: ArrayList<Recipe>
)

@Entity
data class Recipe(
    @SerializedName("analyzedInstructions") val analyzedInstructions: List<AnalyzedInstruction>,
    @SerializedName("cookingMinutes") val cookingMinutes: Int = 0,
    @SerializedName("dishTypes") val dishTypes: List<String> = listOf(""),
    @SerializedName("extendedIngredients") val extendedIngredients: List<ExtendedIngredient>,
    @SerializedName("id") @PrimaryKey val id: Int = 0,
    @SerializedName("image") val image: String = "",
    @SerializedName("instructions") val instructions: String = "",
    @SerializedName("readyInMinutes") val readyInMinutes: Int = 0,
    @SerializedName("servings") val servings: Int = 0,
    @SerializedName("summary") val summary: String = "",
    @SerializedName("title") val title: String = ""
)

data class AnalyzedInstruction(
    val name: String = "",
    val steps: List<Step>
)

data class ExtendedIngredient(
    val aisle: String = "",
    val amount: Double = 0.0,
    val consistency: String = "",
    val id: Int = 0,
    val image: String = "",
    val measures: Measures,
    val meta: List<String> = listOf(""),
    val name: String = "",
    val nameClean: String = "",
    val original: String = "",
    val originalName: String = "",
    val unit: String = ""
)

data class Step(
    val equipment: List<Equipment>,
    val ingredients: List<Ingredient>,
    val length: Length,
    val number: Int = 0,
    val step: String = ""
)

data class Equipment(
    val id: Int = 0,
    val image: String = "",
    val localizedName: String = "",
    val name: String = "",
    val temperature: Temperature
)

data class Ingredient(
    val id: Int = 0,
    val image: String = "",
    val localizedName: String = "",
    val name: String = ""
)

data class Length(
    val number: Int = 0,
    val unit: String = ""
)

data class Temperature(
    val number: Double = 0.0,
    val unit: String = ""
)

data class Measures(
    val metric: Metric,
    val us: Us
)

data class Metric(
    val amount: Double = 0.0,
    val unitLong: String = "",
    val unitShort: String = ""
)

data class Us(
    val amount: Double = 0.0,
    val unitLong: String = "",
    val unitShort: String = ""
)

