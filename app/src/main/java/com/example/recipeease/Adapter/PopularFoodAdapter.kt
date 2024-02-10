package com.example.recipeease.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeease.Model.Meal
import com.example.recipeease.Model.Subcategory
import com.example.recipeease.R
import com.squareup.picasso.Picasso

class PopularFoodAdapter(var foodList:List<Meal>, context:Activity):
    RecyclerView.Adapter<PopularFoodAdapter.PopularFoodViewHolder>() {


    class PopularFoodViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val foodImage: ImageView = itemView.findViewById(R.id.item_img)
        val foodName:TextView = itemView.findViewById(R.id.item_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularFoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.popular_item,parent,false)
        return PopularFoodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: PopularFoodViewHolder, position: Int) {
        val currentItem = foodList[position]

        Picasso.get().load(currentItem.strMealThumb).into(holder.foodImage)
        holder.foodName.text = currentItem.strMeal
    }


}