package com.example.recipesapp.repository

import androidx.lifecycle.LiveData
import com.example.recipesapp.database.RecipeDao
import com.example.recipesapp.models.Recipe

class RecipeRepository(private val recipeDao: RecipeDao) {

    suspend fun insertRecipe(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    val allRecipes: LiveData<List<Recipe>> = recipeDao.getAll()

    suspend fun checkRecipeById(id: Int): Boolean {
        return recipeDao.findById(id)
    }

    suspend fun deleteRecipe(recipe: Recipe) =
        recipeDao.delete(recipe)
}