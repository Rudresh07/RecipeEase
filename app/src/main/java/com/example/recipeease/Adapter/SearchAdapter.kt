package com.example.recipeease.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeease.Model.SearchedData
import com.example.recipeease.Model.searchMeal
import com.example.recipeease.R
import com.squareup.picasso.Picasso

class SearchAdapter(var recipeList:List<searchMeal>,context:Context):
    RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder>() {

    private lateinit var foodItemClick: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        foodItemClick = listener
    }

    class SearchAdapterViewHolder(itemView: View, listener: OnItemClickListener):RecyclerView.ViewHolder(itemView) {

        val foodImage: ImageView = itemView.findViewById(R.id.searched_image)
        val foodName: TextView = itemView.findViewById(R.id.searched_text)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item,parent,false)
        return SearchAdapterViewHolder(view, foodItemClick)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: SearchAdapterViewHolder, position: Int) {
        val currentItem = recipeList[position]

        Picasso.get().load(currentItem.strMealThumb).into(holder.foodImage)
        holder.foodName.text = currentItem.strMeal

        val mealId = currentItem.idMeal
    }
}