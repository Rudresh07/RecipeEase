package com.example.recipeease.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [SavedRecipe::class], version = 1, exportSchema = false)
abstract class AppDatabase :RoomDatabase() {


    abstract fun savedRecipeDao():SavedRecipeDao

    companion object{

        @Volatile
        var INSTANCE : AppDatabase? = null

        fun getDatabaseInstance(context: Context):AppDatabase?{
            val tempInstance = INSTANCE
            if (INSTANCE!=null)return tempInstance
            synchronized(this){
                val roomDB = Room.databaseBuilder(context,AppDatabase::class.java,"AppDatabase").fallbackToDestructiveMigration().build()
                INSTANCE=roomDB

                return roomDB

            }
        }


    }


}