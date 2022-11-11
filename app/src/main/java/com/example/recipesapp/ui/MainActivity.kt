package com.example.recipesapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.repository.Repository
import com.example.recipesapp.viewmodel.FavouritesViewModel
import com.example.recipesapp.viewmodel.FavouritesViewModelFactory
import com.example.recipesapp.viewmodel.RecipesViewModel
import com.example.recipesapp.viewmodel.RecipesViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


//variable to store the button that is pressed
var button: String? = null

//variable to store the current view
var currentView = "default"


class MainActivity : AppCompatActivity() {

    //viewmodels
    lateinit var viewModel: RecipesViewModel
    lateinit var favouritesViewModel: FavouritesViewModel


    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        //create the splashscreen on app startup
        installSplashScreen()
        super.onCreate(savedInstanceState)

        //initialise the viewmodels
        val repository = Repository()
        val viewModelFactory = RecipesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[RecipesViewModel::class.java]

        val favouritesViewModelFactory = FavouritesViewModelFactory(this.application)
        favouritesViewModel =
            ViewModelProvider(this, favouritesViewModelFactory)[FavouritesViewModel::class.java]

        //set the binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set up the navigation
        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)

        //set up top level destinations
        AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_favourites,
            )
        )

        navView.setupWithNavController(navController)
    }


    //function to pass the recipeId to the details fragment when an adapter item is clicked
    fun onSelectedItemClick(id: Int) {
        val bundle = bundleOf("recipeId" to id)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigate(R.id.moveToDetailsFragment, bundle)
    }
}