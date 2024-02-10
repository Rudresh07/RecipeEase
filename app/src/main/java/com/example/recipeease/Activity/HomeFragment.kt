package com.example.recipeease.Activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeease.Adapter.PopularCategoryAdapter
import com.example.recipeease.Adapter.PopularFoodAdapter
import com.example.recipeease.Model.CategoryResponse
import com.example.recipeease.Model.PopularFood
import com.example.recipeease.R
import com.example.recipeease.Retrofit.RetrofitInstance
import com.example.recipeease.databinding.FragmentHomeBinding
import com.example.recipeease.interfaces.GetDataService
import com.example.recipeease.utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class HomeFragment : Fragment() {
    private lateinit var popularCategoryAdapter: PopularCategoryAdapter
    private lateinit var popularFoodAdapter: PopularFoodAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setStatusbarColor()

        // Initialize adapter with an empty list
        popularCategoryAdapter = PopularCategoryAdapter(emptyList(), requireActivity())
        binding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = popularCategoryAdapter
        }


        popularFoodAdapter = PopularFoodAdapter(emptyList(),requireActivity())
        binding.popularRecipeRecycle.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = popularFoodAdapter
        }


        // Fetch categories

           getCategories()

        //get selected item category name
        popularCategoryAdapter.setOnItemClickListener(object :PopularCategoryAdapter.OnItemClickListener
        {
            override fun onItemClicking(position: Int) {
                val clickedCategory = popularCategoryAdapter.categoryList[position]
                val categoryName = clickedCategory.strCategory

                getPopularFood(categoryName)

            }
        })

        binding.editTextText.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        binding.bard.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_AIFragment)
        }

        if (popularCategoryAdapter.categoryList.isEmpty()) {
            getPopularFood("Seafood")
        }


        return binding.root
    }



    private fun getCategories() {
        val service = RetrofitInstance.retrofitInstance.create(GetDataService::class.java)
        val retrofitData = service.getCategoryList()

        retrofitData.enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (response.isSuccessful) {
                    val categoryResponse = response.body()
                    val categoryList = categoryResponse?.categories ?: emptyList()
                    // Update adapter with the fetched category list
                    popularCategoryAdapter.categoryList = categoryList
                    popularCategoryAdapter.notifyDataSetChanged()
                } else {
                    Log.e("getCategories", "Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.e("getCategories", "Failed to get categories", t)
            }
        })
    }

    private fun getPopularFood(categoryDetail:String){
        val service = RetrofitInstance.retrofitInstance.create(GetDataService::class.java)
        val retrofitFoodData = service.getRecipe(categoryDetail)

        retrofitFoodData.enqueue(object :Callback<PopularFood>{
            override fun onResponse(call: Call<PopularFood>, response: Response<PopularFood>) {
                if (response.isSuccessful)
                {

                    val popularFoodResponse =response.body()
                    val foodList = popularFoodResponse?.meals?: emptyList()
                    //Update adapter with fetched popular food
                    popularFoodAdapter.foodList= foodList
                    popularFoodAdapter.notifyDataSetChanged()
                }

                else{
                    Log.e("getPopularFood", "Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PopularFood>, t: Throwable) {
                Log.e("getPopularFood", "Failed to get food", t)
            }

        })
    }


    private fun setStatusbarColor()
    {
        activity?.window?.apply {
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.purple_500)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }


}
