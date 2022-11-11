package com.example.recipesapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.database.RecipeDatabase
import com.example.recipesapp.models.Recipe
import com.example.recipesapp.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesViewModel(application: Application) : AndroidViewModel(application) {

    val recipeDao = RecipeDatabase.getDatabase(application).recipeDao()
    private val repository: RecipeRepository = RecipeRepository(recipeDao)

    //get all the recipes in the database
    val allRecipes: LiveData<List<Recipe>> = repository.allRecipes

    suspend fun checkRecipeById(id: Int): Boolean {
        val result = withContext(Dispatchers.IO) {
            repository.checkRecipeById(id)
        }
        return result
    }

    fun insertRecipe(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertRecipe(recipe)
        }
    }

    suspend fun delete(recipe: Recipe) {
        withContext(Dispatchers.IO) {
            repository.deleteRecipe(recipe)
        }
    }
}