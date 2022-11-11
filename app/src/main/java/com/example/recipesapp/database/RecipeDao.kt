package com.example.recipesapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.recipesapp.models.Recipe

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipe")
    fun getAll(): LiveData<List<Recipe>>

    @Query("SELECT COUNT(*) FROM recipe WHERE id = :id")
    suspend fun findById(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

}