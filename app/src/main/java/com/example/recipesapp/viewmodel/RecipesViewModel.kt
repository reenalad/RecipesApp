package com.example.recipesapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipesapp.models.Recipe
import com.example.recipesapp.models.Recipes
import com.example.recipesapp.repository.Repository
import com.example.recipesapp.utils.Constants.Companion.API_KEY
import com.example.recipesapp.utils.Constants.Companion.QUERY
import com.example.recipesapp.utils.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.recipesapp.utils.Constants.Companion.QUERY_API_KEY
import com.example.recipesapp.utils.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.recipesapp.utils.Constants.Companion.QUERY_INSTRUCTIONS
import com.example.recipesapp.utils.Constants.Companion.QUERY_NUMBER
import com.example.recipesapp.utils.Constants.Companion.QUERY_TYPE
import kotlinx.coroutines.launch
import retrofit2.Response

//viewmodel for recipes
class RecipesViewModel(private val repository: Repository) : ViewModel() {
    var recipesResponse: MutableLiveData<Response<Recipes>> = MutableLiveData()
    var individualRecipe: MutableLiveData<Response<Recipe>> = MutableLiveData()

    //function to get the recipes
    fun getRecipes(apiKey: String, number: Int) {
        viewModelScope.launch {
            val response = repository.getRecipes(apiKey, number)
            recipesResponse.value = response
        }
    }

    fun getCourseRecipes(query: HashMap<String, String>) {
        viewModelScope.launch {
            val response = repository.getCourseRecipes(query)
            recipesResponse.value = response
        }
    }

    fun getRecipeById(id: Int, apiKey: String) {
        viewModelScope.launch {
            val response = repository.getRecipeById(id, apiKey)
            individualRecipe.value = response

        }
    }

    fun applyQueries(courseType: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = courseType
        queries[QUERY_INSTRUCTIONS] = "true"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun applySearchQueries(searchTerm: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY] = searchTerm
        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_INSTRUCTIONS] = "true"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
}