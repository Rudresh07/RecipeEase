package com.example.recipeease.Activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.recipeease.R

class AI_Recipe_Detail : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_a_i__recipe__detail, container, false)




        // Get the recipe data from the arguments
        val recipeResult = arguments?.getString("recipe_result")



        //val RecipeTitle = view.findViewById<TextView>(R.id.recipe_title)
        val RecipeIngredient = view.findViewById<TextView>(R.id.ing_Data)
       // val RecipeStep = view.findViewById<TextView>(R.id.recipe_steps)



       // RecipeTitle.text = dishName

       RecipeIngredient.text = recipeResult

        //RecipeStep.text = stepsText


//        val ingredientButton = view.findViewById<Button>(R.id.ingredient)
//        val stepsButton = view.findViewById<Button>(R.id.steps)
//        val ingredientScroll = view.findViewById<ScrollView>(R.id.ingredientScroll)
//        val stepScroll = view.findViewById<ScrollView>(R.id.StepsScroll)

//        ingredientButton.setOnClickListener {
//            // Set the background and text color of the clicked button
//            ingredientButton.setBackgroundResource(R.drawable.btn_ing)
//            ingredientButton.setTextColor(resources.getColor(R.color.white))
//
//            // Reset the background and text color of the other buttons
//            stepsButton.setTextColor(resources.getColor(R.color.black))
//            stepsButton.background = null
//
//            // Show/hide the appropriate views
//            stepScroll.visibility = View.GONE
//            ingredientScroll.visibility = View.VISIBLE
//        }
//
//        stepsButton.setOnClickListener {
//            // Set the background and text color of the clicked button
//            stepsButton.setBackgroundResource(R.drawable.btn_ing)
//            stepsButton.setTextColor(resources.getColor(R.color.white))
//
//            // Reset the background and text color of the other buttons
//            ingredientButton.setTextColor(resources.getColor(R.color.black))
//            ingredientButton.background = null
//
//            // Show/hide the appropriate views
//           stepScroll.visibility = View.VISIBLE
//            ingredientScroll.visibility = View.GONE
//        }




        val back_btn = view.findViewById<ImageView>(R.id.back_btn)

        back_btn.setOnClickListener {

            navigateToFragment(AIFragment())
        }



Log.d("recipe", "$recipeResult")

        return view


    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
