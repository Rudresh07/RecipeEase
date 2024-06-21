package com.example.recipeease.Adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeease.Model.Meal
import com.example.recipeease.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class PopularFoodAdapter(
    var foodList: List<Meal>,
    private val context: Activity
) : RecyclerView.Adapter<PopularFoodAdapter.PopularFoodViewHolder>() {

    private lateinit var foodItemClick: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        foodItemClick = listener
    }

    class PopularFoodViewHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        val foodImage: ImageView = itemView.findViewById(R.id.item_img)
        val foodName: TextView = itemView.findViewById(R.id.item_name)
        val saveButton: ImageView = itemView.findViewById(R.id.SaveLocal)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularFoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.popular_item, parent, false)
        return PopularFoodViewHolder(view, foodItemClick)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: PopularFoodViewHolder, position: Int) {
        val currentItem = foodList[position]

        Picasso.get().load(currentItem.strMealThumb).into(holder.foodImage)
        holder.foodName.text = currentItem.strMeal

        // Check if meal is bookmarked and update bookmark icon accordingly
        isMealBookmarked(currentItem.idMeal) { isBookmarked ->
            val bookmarkIcon = if (isBookmarked) R.drawable.bookmark else R.drawable.bookmarkborder
            holder.saveButton.setImageResource(bookmarkIcon)
        }

        // Set click listener for bookmark button
        holder.saveButton.setOnClickListener {
            val meal = foodList[position]
            toggleBookmark(meal) { success ->
                if (success) {
                    showToast("Bookmarked successfully")
                } else {
                    showToast("Failed to bookmark")
                }
            }
        }
    }

    private fun toggleBookmark(meal: Meal, callback: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            val userBookmarkRef = db.collection("users").document(userId)
                .collection("bookmarks").document(meal.idMeal)

            userBookmarkRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    // Recipe is bookmarked, remove bookmark
                    userBookmarkRef.delete().addOnSuccessListener {
                        callback(true) // Success
                    }.addOnFailureListener {exception ->
                        Log.d("toggleBookmark", "Error deleting bookmark: $exception")
                        callback(false) // Error
                    }
                } else {
                    // Recipe is not bookmarked, add bookmark
                    val mealData = hashMapOf(
                        "idMeal" to meal.idMeal,
                        "strMeal" to meal.strMeal,
                        "strMealThumb" to meal.strMealThumb
                    )
                    userBookmarkRef.set(mealData).addOnSuccessListener {
                        callback(true) // Success
                    }.addOnFailureListener {exception ->
                        Log.d("toggleBookmark", "Error adding bookmark: $exception")
                        callback(false) // Error
                    }
                }
            }.addOnFailureListener {exception ->
                Log.d("toggleBookmark", "Error getting document: $exception")
                callback(false) // Error
            }
        } else {
            callback(false) // Return false if user is not authenticated
        }
    }

    private fun isMealBookmarked(recipeId: String, callback: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            val userBookmarkRef = db.collection("users").document(userId)
                .collection("bookmarks").document(recipeId)

            userBookmarkRef.get().addOnSuccessListener { documentSnapshot ->
                val isBookmarked = documentSnapshot.exists()
                callback(isBookmarked)
            }.addOnFailureListener { exception ->
                // Handle failure
                exception.printStackTrace()
                callback(false) // Return false in case of error
            }
        } else {
            callback(false) // Return false if user is not authenticated
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
