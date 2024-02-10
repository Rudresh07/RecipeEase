package com.example.recipeease.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeease.Model.Subcategory
import com.example.recipeease.R
import com.squareup.picasso.Picasso

class PopularCategoryAdapter(var categoryList:List<Subcategory>, context:Activity):
    RecyclerView.Adapter<PopularCategoryAdapter.CategoryViewHolder>() {

        private lateinit var catogryItemClick : OnItemClickListener

    interface OnItemClickListener {

        fun onItemClicking(position: Int)

    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        catogryItemClick = listener
    }


        class CategoryViewHolder(itemView:View,listener:OnItemClickListener):RecyclerView.ViewHolder(itemView) {
            val categoryImage: ImageView = itemView.findViewById(R.id.categoryImage)
            val categoryName: TextView = itemView.findViewById(R.id.categoryName)

            init {
                itemView.setOnClickListener {
                    listener.onItemClicking(adapterPosition)
                }
            }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularCategoryAdapter.CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_category,parent,false)
        return CategoryViewHolder(view,catogryItemClick)
    }

    override fun onBindViewHolder(
        holder: PopularCategoryAdapter.CategoryViewHolder,
        position: Int
    ) {
        val currentItem = categoryList[position]

        holder.categoryName.text = currentItem.strCategory
        Picasso.get().load(currentItem.strCategoryThumb).into(holder.categoryImage)

    }

    override fun getItemCount(): Int {
       return categoryList.size
    }


}