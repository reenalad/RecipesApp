package com.example.recipesapp.repository

import com.example.recipesapp.api.RetrofitInstance
import com.example.recipesapp.models.Recipe
import com.example.recipesapp.models.Recipes
import retrofit2.Response

//repository to manage data
class Repository {

    //get random recipes
    suspend fun getRecipes(apiKey: String, number: Int): Response<Recipes> {
        return RetrofitInstance.api.getRecipes(apiKey, number)
    }

    suspend fun getCourseRecipes(query: Map<String, String>): Response<Recipes> {
        return RetrofitInstance.api.getCourseRecipes(query)
    }

    suspend fun getRecipeById(id: Int, apiKey: String): Response<Recipe> {
        return RetrofitInstance.api.getRecipeById(id, apiKey)
    }

}