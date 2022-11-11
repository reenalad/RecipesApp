package com.example.recipesapp.ui

import android.accounts.NetworkErrorException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.R
import com.example.recipesapp.adapter.RecipesAdapter
import com.example.recipesapp.databinding.FragmentFavouritesBinding
import com.example.recipesapp.models.Recipe
import com.example.recipesapp.models.Recipes
import com.example.recipesapp.viewmodel.FavouritesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_home.*


class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    //set up the adapter
    var recipesAdapter = RecipesAdapter { (activity as MainActivity).onSelectedItemClick(it.id) }

    //access the favourites viewmodel
    val favouritesViewModel: FavouritesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //show the bottom navigation
        val navBar: BottomNavigationView? = getActivity()?.findViewById(R.id.nav_view)
        if (navBar != null) {
            navBar.visibility = View.VISIBLE
        }

        //inflate the layout for this fragment
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setup the recyclerview
        recyclerView.adapter = recipesAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        try {
            //get the favourite recipes from the database
            favouritesViewModel.allRecipes.observe(viewLifecycleOwner, Observer { recipeList ->
                if (recipeList.isNotEmpty()) {
                    //if there are recipes, hide/clear the error views
                    clearError()
                    //store the recipes in an arraylist
                    val recipeArrayList = arrayListOf<Recipe>()
                    for (recipe in recipeList) {
                        recipeArrayList.add(recipe as Recipe)
                    }
                    //create a recipes instance with the arraylist
                    val recipes = Recipes(recipeArrayList)
                    binding.noFavouritesText.text = ""
                    //display the recipes
                    recipes.let { recipesAdapter.setData(it) }
                } else {
                    //if there are no recipes, hide the recyclerview
                    binding.recyclerView.visibility = View.INVISIBLE
                    //display a message to the user
                    binding.noFavouritesText.text = "No favourites added yet!"
                }
            })
        } catch (e: Exception) {
            displayGenericError()
        } catch (e: NetworkErrorException) {
            displayNetworkError()
        }

    }

    //show the generic error image and message
    fun displayGenericError() {
        binding.recyclerView.visibility = View.INVISIBLE
        binding.errorImage.visibility = View.VISIBLE
        binding.errorImage.setImageResource(R.drawable.ic_baseline_broken_image_24)
        binding.errorText.text = "Unable to display data"
    }

    //show the network error and image
    fun displayNetworkError() {
        binding.recyclerView.visibility = View.INVISIBLE
        binding.errorImage.visibility = View.VISIBLE
        binding.errorImage.setImageResource(R.drawable.no_network)
        binding.errorText.text = "No network connection"
    }

    //hide/clear the error views
    fun clearError() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.errorImage.visibility = View.INVISIBLE
        binding.errorText.text = ""
    }
}