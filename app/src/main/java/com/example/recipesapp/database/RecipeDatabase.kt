package com.example.recipesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipesapp.models.Recipe

@Database(entities = [Recipe::class], version = 1, exportSchema = true)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            //if the instance is not null, then return it, if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    //pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            //return database.
            return INSTANCE!!
        }

        //build the database
        private fun buildDatabase(context: Context): RecipeDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                RecipeDatabase::class.java,
                "recipes"
            ).build()
        }
    }
}