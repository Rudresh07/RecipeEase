package com.example.recipeease.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "SavedRecipe")
    data class SavedRecipe(@PrimaryKey
                           val idMeal: String,
                           val strMeal: String,
                           val strMealThumb: String,
    var isBookMarked:Boolean = false)


