package com.example.recipesapp.api

import com.example.recipesapp.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RecipesApi {

    //api call to get random recipes
    @GET("recipes/random")
    suspend fun getRecipes(
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int
    ): Response<Recipes>

    //get course or search recipes
    @GET("/recipes/complexSearch")
    suspend fun getCourseRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<Recipes>

    //get individual recipe details
    @GET("/recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String
    ): Response<Recipe>

}