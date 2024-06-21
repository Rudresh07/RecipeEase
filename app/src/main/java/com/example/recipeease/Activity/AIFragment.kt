package com.example.recipeease.Activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.recipeease.BuildConfig
import com.example.recipeease.R
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.runBlocking

class AIFragment : Fragment() {

    private lateinit var editTextContainer: LinearLayout
    private var editTextCount = 0

    val apiKey = BuildConfig.API_KEY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {





        val view = inflater.inflate(R.layout.fragment_a_i, container, false)

        // Initialize views
        editTextContainer = view.findViewById(R.id.editTextContainer)
        val addButton = view.findViewById<Button>(R.id.addButton)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)
        val submitButton = view.findViewById<Button>(R.id.submitButton) // Add submit button


        // Set click listeners
        addButton.setOnClickListener { addEditText() }
        deleteButton.setOnClickListener { deleteEditText() }


        submitButton.setOnClickListener {
            var result = ""

            val ingredientString = getIngredientsString()

            val generativeModel = GenerativeModel(
                // For text-only input, use the gemini-pro model
                modelName = "gemini-pro",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                apiKey = apiKey
            )

            val prompt = "I have $ingredientString. What can I prepare form it tell steps to prepare the meal."

            runBlocking {
                val response = generativeModel.generateContent(prompt)
                result = response.text.toString()
            }

            // Create a Bundle to hold the recipe data (result string)
            val bundle = Bundle()
            bundle.putString("recipe_result", result)

            // Navigate to the destination fragment with arguments

            navigateToFragment(AI_Recipe_Detail(),bundle)
        }


        return view
    }


    private fun navigateToFragment(fragment: Fragment, bundle: Bundle?) {
        val transaction = parentFragmentManager.beginTransaction()
        if (bundle != null) {
            fragment.arguments = bundle
        }
        transaction.replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }


    private fun getIngredientsString(): String {
        val ingredients = StringBuilder()
        for (i in 0 until editTextContainer.childCount) {
            val editText = editTextContainer.getChildAt(i) as EditText
            val ingredientText = editText.text.toString().trim()
            if (ingredientText.isNotEmpty()) {
                if (ingredients.isNotEmpty()) {
                    ingredients.append(",")
                }
                ingredients.append(ingredientText)
            }
        }
        return ingredients.toString()
    }



    private fun addEditText() {
        editTextCount++

        val editText = EditText(requireContext())
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        // Add margins
        // Adjust the dimension resource as per your requirement
        layoutParams.setMargins(60,10, 60, 10)

        editText.layoutParams = layoutParams
        editText.hint = "Enter Ingredient $editTextCount"

        editText.setBackgroundResource(R.drawable.searchview)

        val paddingPx = resources.getDimensionPixelSize(R.dimen.edit_text_padding) // Adjust the dimension resource as per your requirement
        editText.setPadding(paddingPx, paddingPx, paddingPx, paddingPx)

        editTextContainer.addView(editText)

    }

    private fun deleteEditText() {
        if (editTextContainer.childCount > 0) {
            editTextContainer.removeViewAt(editTextContainer.childCount - 1)
            editTextCount--
        }
    }


}
