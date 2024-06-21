package com.example.recipeease

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeease.Activity.RecipeDetail
import com.example.recipeease.Adapter.SavedRecipeAdapter
import com.example.recipeease.Model.SavedMeal
import com.example.recipeease.databinding.FragmentSavedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SavedFragment : Fragment() {
    private lateinit var binding: FragmentSavedBinding
    private lateinit var savedRecipeAdapter: SavedRecipeAdapter
    private lateinit var savedMealList: MutableList<SavedMeal>
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        savedMealList = mutableListOf()
        savedRecipeAdapter = SavedRecipeAdapter(savedMealList, requireActivity())

        binding.SavedRecipeRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = savedRecipeAdapter
        }

        fetchSavedMeal()

        savedRecipeAdapter.setOnItemClickListener(object : SavedRecipeAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickedFood = savedMealList[position] // Corrected this reference
                val foodId = clickedFood.idMeal
                val intent = Intent(requireContext(), RecipeDetail::class.java).apply {
                    putExtra("FoodId", foodId)
                }
                startActivity(intent)
            }
        })

        return binding.root
    }

    private fun fetchSavedMeal() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(userId)
                .collection("bookmarks")
                .get()
                .addOnSuccessListener { documents ->
                    savedMealList.clear()
                    for (document in documents) {
                        val savedMeal = document.toObject(SavedMeal::class.java)
                        savedMealList.add(savedMeal)
                    }
                    savedRecipeAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to fetch saved recipes", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Failed to fetch saved recipes", Toast.LENGTH_SHORT).show()
        }
    }
}
