package com.example.recipeease.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.recipeease.AddNewRecipeFragment
import com.example.recipeease.R
import com.example.recipeease.SavedFragment
import com.example.recipeease.SettingsFragment
import com.example.recipeease.databinding.ActivityMainBinding
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var savedFragment: SavedFragment
    private lateinit var recipeFragment: AddNewRecipeFragment
    private lateinit var settingsFragment: SettingsFragment

    companion object {
        const val HOME_ITEM = 1
        const val SAVED_ITEM = 2
        const val ADD_RECIPE_ITEM = 3
        const val SETTINGS_ITEM = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeFragment = HomeFragment()
        savedFragment = SavedFragment()
        recipeFragment = AddNewRecipeFragment()
        settingsFragment = SettingsFragment()



        val bottomNavigation = binding.bottomNavigation

        bottomNavigation.add(
            CurvedBottomNavigation.Model(HOME_ITEM, "Home", R.drawable.home)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(SAVED_ITEM, "Saved", R.drawable.bookmark)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(ADD_RECIPE_ITEM, "Add Recipe", R.drawable.add)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(SETTINGS_ITEM, "Settings", R.drawable.baseline_settings_24)
        )

        // Set the default selected item to Home
        bottomNavigation.show(HOME_ITEM)

        bottomNavigation.setOnClickMenuListener { item ->
            when (item.id) {
                HOME_ITEM -> {
                    replaceFragment(homeFragment)
                }
                SAVED_ITEM -> {
                    replaceFragment(savedFragment)
                }
                ADD_RECIPE_ITEM -> {
                    replaceFragment(recipeFragment)
                }
                SETTINGS_ITEM -> {
                    replaceFragment(settingsFragment)
                }
            }
            true
        }

        fragmentManager = supportFragmentManager
        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}
