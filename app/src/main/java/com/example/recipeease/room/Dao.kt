package com.example.recipeease.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(savedRecipe: SavedRecipe)

    @Query("SELECT * FROM SavedRecipe")

    fun getSavedRecipe(): LiveData<List<SavedRecipe>>

    @Query("DELETE FROM SavedRecipe WHERE idMeal = :id")
    fun deleteRecipe(id:String)


}