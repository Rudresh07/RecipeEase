package com.example.recipeease.Activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        //get selected item id

        popularFoodAdapter.setOnItemClickListener(object :PopularFoodAdapter.OnItemClickListener
        {
            override fun onItemClick(position: Int) {
                val clickedFood = popularFoodAdapter.foodList[position]
                val foodId = clickedFood.idMeal

                val intent = Intent(requireContext(),RecipeDetail::class.java).apply {
                    putExtra("FoodId",foodId)
                }
                startActivity(intent)
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

        binding.NavView.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.app_info)

            dialog.show()
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setGravity(Gravity.BOTTOM)

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
