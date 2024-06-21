package com.example.recipeease.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeease.Adapter.SearchAdapter
import com.example.recipeease.Model.SearchedData
import com.example.recipeease.Model.searchMeal
import com.example.recipeease.R
import com.example.recipeease.Retrofit.RetrofitInstance
import com.example.recipeease.databinding.FragmentSearchBinding
import com.example.recipeease.interfaces.GetDataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private var initialSearchList: List<searchMeal> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        searchAdapter = SearchAdapter(emptyList(), requireContext())
        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = searchAdapter
        }
        getSearchedFood("a")
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s?.toString()?.trim() ?: ""
                if (searchText.length <= 1) {
                    getSearchedFood(searchText)
                } else {
                    filterSearchList(searchText)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchAdapter.setOnItemClickListener(object :SearchAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val clickedFood = searchAdapter.recipeList[position]
                val foodId = clickedFood.idMeal

                val intent = Intent(requireContext(),RecipeDetail::class.java).apply {
                    putExtra("FoodId",foodId)
                }
                startActivity(intent)
            }

        })


        binding.BackImage.setOnClickListener {
            navigateToFragment(HomeFragment())
        }
        return binding.root
    }

    private fun getSearchedFood(firstLetter: String) {
        val service = RetrofitInstance.retrofitInstance.create(GetDataService::class.java)
        val retrofitSearchedData = service.searchRecipe(firstLetter)
        retrofitSearchedData.enqueue(object : Callback<SearchedData> {
            override fun onResponse(call: Call<SearchedData>, response: Response<SearchedData>) {
                if (response.isSuccessful) {
                    val searchResponse = response.body()
                    initialSearchList = searchResponse?.meals ?: emptyList()
                    searchAdapter.recipeList = initialSearchList
                    searchAdapter.notifyDataSetChanged()
                } else {
                    Log.e("getSearchedFood", "Unsuccessful response: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<SearchedData>, t: Throwable) {
                Log.e("getSearchedFood", "Failed to get food", t)
            }
        })
    }

    private fun filterSearchList(searchText: String) {
        val filteredList = initialSearchList.filter { meal ->
            meal.strMeal.toLowerCase().contains(searchText.toLowerCase())
        }
        searchAdapter.recipeList = filteredList
        searchAdapter.notifyDataSetChanged()
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
