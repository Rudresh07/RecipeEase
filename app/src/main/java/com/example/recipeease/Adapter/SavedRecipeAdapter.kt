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
import com.example.recipeease.Model.SavedMeal
import com.example.recipeease.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class SavedRecipeAdapter(
    private var savedList: MutableList<SavedMeal>, // Make this MutableList to allow modifications
    private val context: Activity
) : RecyclerView.Adapter<SavedRecipeAdapter.SavedRecipeViewHolder>() {

    private lateinit var savedItemClick: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        savedItemClick = listener
    }

    class SavedRecipeViewHolder(itemView: View, listener: OnItemClickListener) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.saved_recipe, parent, false)
        return SavedRecipeViewHolder(view, savedItemClick)
    }

    override fun onBindViewHolder(holder: SavedRecipeViewHolder, position: Int) {
        val currentItem = savedList[position]

        Picasso.get().load(currentItem.strMealThumb).into(holder.foodImage)
        holder.foodName.text = currentItem.strMeal

        // No need to check if meal is bookmarked since this list is only for bookmarked meals

        // Set click listener for bookmark button to remove from bookmarks
        holder.saveButton.setOnClickListener {
            removeBookmark(currentItem) { success ->
                if (success) {
                    removeItem(position)
                    showToast("Removed from bookmarks successfully")
                } else {
                    showToast("Failed to remove from bookmarks")
                }
            }
        }
    }

    private fun removeBookmark(meal: SavedMeal, callback: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            val userBookmarkRef = db.collection("users").document(userId)
                .collection("bookmarks").document(meal.idMeal)

            userBookmarkRef.delete().addOnSuccessListener {
                callback(true) // Success
            }.addOnFailureListener { exception ->
                Log.d("removeBookmark", "Error deleting bookmark: $exception")
                callback(false) // Error
            }
        } else {
            callback(false) // Return false if user is not authenticated
        }
    }

    private fun removeItem(position: Int) {
        savedList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, savedList.size)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        return savedList.size
    }
}
