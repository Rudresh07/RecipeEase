package com.example.recipeease.Activity

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import com.example.recipeease.Model.RecipeDetailData
import com.example.recipeease.Model.searchMeal
import com.example.recipeease.R
import com.example.recipeease.Retrofit.RetrofitInstance
import com.example.recipeease.databinding.ActivityRecipeDetailBinding
import com.example.recipeease.interfaces.GetDataService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeDetail : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailBinding
    var imgCrop = true
    var youtubeLink:String = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val foodId = intent.getStringExtra("FoodId")

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.fullScreen.setOnClickListener {
            if (imgCrop) {
                binding.MealImage.scaleType = ImageView.ScaleType.FIT_CENTER
                binding.shade.visibility = View.GONE
                binding.fullScreen.setColorFilter(
                    android.graphics.Color.BLACK,
                    PorterDuff.Mode.SRC_ATOP
                )
                imgCrop = !imgCrop
            } else {
                binding.MealImage.scaleType = ImageView.ScaleType.CENTER_CROP

                binding.fullScreen.setColorFilter(null)
                imgCrop = !imgCrop
            }
        }

        foodId?.let { getRecipeDetail(it.toInt()) }

        binding.steps.background = null
        binding.steps.setTextColor(getColor(R.color.black))
        binding.Video.background = null
        binding.Video.setTextColor(getColor(R.color.black))

        binding.steps.setOnClickListener {
            binding.steps.setBackgroundResource(R.drawable.btn_ing)
            binding.steps.setTextColor(getColor(R.color.white))
            binding.ingredient.setTextColor(getColor(R.color.black))
            binding.ingredient.background = null
            binding.Video.setTextColor(getColor(R.color.black))
            binding.Video.background = null
            binding.ingredientScroll.visibility = View.GONE
            binding.StepsScroll.visibility = View.VISIBLE
            binding.webView.visibility = View.GONE
        }

        binding.ingredient.setOnClickListener {
            binding.ingredient.setBackgroundResource(R.drawable.btn_ing)
            binding.ingredient.setTextColor(getColor(R.color.white))
            binding.steps.setTextColor(getColor(R.color.black))
            binding.steps.background = null
            binding.Video.setTextColor(getColor(R.color.black))
            binding.Video.background = null
            binding.StepsScroll.visibility = View.GONE
            binding.ingredientScroll.visibility = View.VISIBLE
            binding.webView.visibility = View.GONE
        }

        binding.Video.setOnClickListener {
            binding.Video.setBackgroundResource(R.drawable.btn_ing)
            binding.Video.setTextColor(getColor(R.color.white))
            binding.steps.setTextColor(getColor(R.color.black))
            binding.steps.background = null
            binding.ingredient.setTextColor(getColor(R.color.black))
            binding.ingredient.background = null
            binding.StepsScroll.visibility = View.GONE
            binding.ingredientScroll.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE


            // Load the YouTube video URL into the WebView

            val youtubeVideoUrl = youtubeLink
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.loadUrl(youtubeVideoUrl)
        }


    }



    fun getRecipeDetail(ID: Int) {
        val service = RetrofitInstance.retrofitInstance.create(GetDataService::class.java)
        val RecipeData = service.recipeDetail(ID)
        RecipeData.enqueue(object : Callback<RecipeDetailData> {
            override fun onResponse(
                call: Call<RecipeDetailData>,
                response: Response<RecipeDetailData>
            ) {
                if (response.isSuccessful) {
                    val recipeResponse = response.body()
                    val recipeList = recipeResponse?.meals ?: emptyList()
                    // Update UI with the fetched data
                    displayRecipeDetails(recipeList)
                } else {
                    // Handle unsuccessful response
                    Log.e("getRecipeDetail", "Unsuccessful response: ${response.code()}")
                    // You might want to handle this case appropriately, like showing an error message
                }
            }


            override fun onFailure(call: Call<RecipeDetailData>, t: Throwable) {
// Handle failure
                Log.e("getRecipeDetail", "Failed to fetch recipe detail", t)
            }

        })
    }

    private fun displayRecipeDetails(recipeList: List<searchMeal>) {
        // Update your UI with the fetched recipe details
        // For example:
        val firstRecipe = recipeList.firstOrNull()
        val instructions = firstRecipe?.strInstructions ?: "No instructions available"
        val lines = instructions.split("\n")

        val modifiedInstructions = buildString {
            for ((index, line) in lines.withIndex()) {
                if (line.isNotEmpty()) {
                    append("🟢 ")
                    append(line)
                } else {
                    append(line)
                }
                if (index != lines.lastIndex) {
                    append("\n")
                }
            }
        }

        binding.recipeSteps.text = modifiedInstructions

        val MealImage = findViewById<ImageView>(R.id.MealImage)
        Picasso.get().load(firstRecipe?.strMealThumb).into(MealImage)

        binding.recipeTitle.text = firstRecipe?.strMeal?: "No title available"


        youtubeLink = firstRecipe?.strYoutube?:"No Link Available"


        // Concatenate all ingredients and measures
        val ingredientsText = StringBuilder()
        for (i in 1..20) {
            val ingredient = firstRecipe?.getIngredient(i)
            val measure = firstRecipe?.getMeasure(i)
            if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) {
                ingredientsText.append("🟢$ingredient - $measure\n")
            }
        }
        // Set concatenated ingredients to TextView
        binding.ingData.text = ingredientsText.toString().trim()
    }


    // Extension functions to get ingredient and measure dynamically
    fun searchMeal.getIngredient(index: Int): String? {
        return when (index) {
            1 -> strIngredient1
            2 -> strIngredient2
            3 -> strIngredient3
            4 -> strIngredient4
            5 -> strIngredient5
            6 -> strIngredient6
            7 -> strIngredient7
            8 -> strIngredient8
            9 -> strIngredient9
            10 -> strIngredient10
            11 -> strIngredient11
            12 -> strIngredient12
            13 -> strIngredient13
            14 -> strIngredient14
            15 -> strIngredient15
            16 -> strIngredient16
            17 -> strIngredient17
            18 -> strIngredient18
            19 -> strIngredient19
            20 -> strIngredient20
            else -> null
        }
    }

    fun searchMeal.getMeasure(index: Int): String? {
        return when (index) {
            1 -> strMeasure1
            2 -> strMeasure2
            3 -> strMeasure3
            4 -> strMeasure4
            5 -> strMeasure5
            6 -> strMeasure6
            7 -> strMeasure7
            8 -> strMeasure8
            9 -> strMeasure9
            10 -> strMeasure10
            11 -> strMeasure11
            12 -> strMeasure12
            13 -> strMeasure13
            14 -> strMeasure14
            15 -> strMeasure15
            16 -> strMeasure16
            17 -> strMeasure17
            18 -> strMeasure18
            19 -> strMeasure19
            20 -> strMeasure20
            else -> null
        }
    }
}