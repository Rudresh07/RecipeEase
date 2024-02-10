package com.example.recipeease

import android.content.Context
import android.widget.Toast

object utils {

    fun ShowToast(context: Context, message:String)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}