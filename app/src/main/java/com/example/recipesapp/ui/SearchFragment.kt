package com.example.recipesapp.ui

import android.accounts.NetworkErrorException
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.MotionEffect.TAG
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipesapp.R
import com.example.recipesapp.adapter.RecipesAdapter
import com.example.recipesapp.databinding.FragmentSearchBinding
import com.example.recipesapp.utils.Constants
import com.example.recipesapp.viewmodel.RecipesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    val binding get() = _binding!!

    //access the recipes viewmodel
    private val viewModel: RecipesViewModel by activityViewModels()

    //set up the adapter
    private val recipesAdapter =
        RecipesAdapter { (activity as MainActivity).onSelectedItemClick(it.id) } //by lazy {RecipesAdapter()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //show the bottom navigation
        val navBar: BottomNavigationView? = getActivity()?.findViewById(R.id.nav_view)
        if (navBar != null) {
            navBar.visibility = View.VISIBLE
        }

        //set the value of currentView, so correct layout is displayed
        currentView = "search"

        //inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up the recyclerview
        recyclerView.adapter = recipesAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        //make the search box fully visible
        binding.searchBox.isIconified = false

        try {
            //make api call to get random recipes and observe the livedata
            viewModel.getRecipes(Constants.API_KEY, 50)
            //viewModel.getCustomPosts(5,"id", "desc")
            viewModel.recipesResponse.observe((activity as MainActivity), Observer { response ->
                if (response.isSuccessful) {
                    //display the recipes
                    response.body()?.let { recipesAdapter.setData(it) }
                } else {
                    displayGenericError()
                    Log.d(TAG, "Error with code: " + response.code().toString())
                }
            })
        } catch (e: Exception) {
            displayGenericError()
        } catch (e: NetworkErrorException) {
            displayNetworkError()
        }

        //when the user submits a search term, search for recipes
        binding.searchBox.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.statusMessage.text = ""
                try {
                    //get recipes using the search term
                    viewModel.getCourseRecipes(viewModel.applySearchQueries(query))
                    //observe the livedata
                    viewModel.recipesResponse.observe(
                        (activity as MainActivity),
                        Observer { response ->
                            if (response.isSuccessful) {
                                //clear/hide error views if the response is successful
                                clearError()
                                if (response.body()?.recipes?.size!! > 0) {
                                    //if there are recipes, display these
                                    response.body()?.let { recipesAdapter.setData(it) }
                                } else {
                                    //display a message if no recipes were found using the search term
                                    binding.statusMessage.text =
                                        "There were no " + query + " recipes"
                                }
                            } else {
                                //if there was an error, display error message
                                displayGenericError()
                            }
                        })
                } catch (e: Exception) {
                    displayGenericError()
                } catch (e: NetworkErrorException) {
                    displayNetworkError()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

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

    override fun onStop() {
        super.onStop()
        //change the value of currentView when this view is stopped
        currentView = "other"
    }
}

