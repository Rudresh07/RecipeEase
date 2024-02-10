package com.example.recipeease.interfaces


import com.example.recipeease.Model.CategoryResponse
import com.example.recipeease.Model.PopularFood
import com.example.recipeease.Model.RecipeDetail
import com.example.recipeease.Model.SearchedData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDataService {

    @GET("categories.php")
    fun getCategoryList(): Call<CategoryResponse>

    @GET("filter.php")
    fun getRecipe(@Query("c") category: String): Call<PopularFood>

    @GET("search.php")
    fun searchRecipe(@Query("f") letter:String):Call<SearchedData>

    @GET("lookup.php")
    fun recipeDetail(@Query("i")id:Int):Call<RecipeDetail>
}