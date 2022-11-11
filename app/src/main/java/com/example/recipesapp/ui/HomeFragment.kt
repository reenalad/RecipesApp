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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.R
import com.example.recipesapp.adapter.RecipesAdapter
import com.example.recipesapp.databinding.FragmentHomeBinding
import com.example.recipesapp.utils.Constants.Companion.API_KEY
import com.example.recipesapp.viewmodel.RecipesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_home.*

//variable to keep track of selected button
lateinit var selectedButton: MaterialButton

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //access the recipes viewmodel
    private val viewModel: RecipesViewModel by activityViewModels()

    //setup the adapter
    private var recipesAdapter =
        RecipesAdapter { (activity as MainActivity).onSelectedItemClick(it.id) }


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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up the recyclerview
        recyclerView.adapter = recipesAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        //set the data, depending on which button is selected
        when (button) {
            null, "randomButton" -> setRandomButton()
            "startersButton" -> setStartersButton()
            "mainsButton" -> setMainsButton()
            else -> setDessertsButton()
        }

        //when the starters button is clicked, get and display starters
        binding.startersButton.setOnClickListener {
            setStartersButton()
        }

        //when mains is clicked, show main courses
        binding.mainsButton.setOnClickListener {
            setMainsButton()
        }

        //when desserts is clicked, display desserts
        binding.dessertsButton.setOnClickListener {
            setDessertsButton()
        }

        //when random is clicked, show random recipes
        binding.randomButton.setOnClickListener {
            setRandomButton()
        }
    }

    //get random recipes from spoonacular
    fun getRandomRecipes() {
        try {
            //make the api call and observe the livedata
            viewModel.getRecipes(API_KEY, 50)
            viewModel.recipesResponse.observe((activity as MainActivity), Observer { response ->
                if (response.isSuccessful) {
                    //if the response is successful, hide/clear the error views
                    clearError()
                    //display the data
                    response.body()?.let { recipesAdapter.setData(it) }
                } else {
                    //if the api call is not successful, display error message to user
                    displayGenericError()
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

    //get recipe by course
    fun getCourse(course: String) {
        try {
            //make api call to get specific course and observe the livedata
            viewModel.getCourseRecipes(viewModel.applyQueries(course))
            //viewModel.getCustomPosts(5,"id", "desc")
            viewModel.recipesResponse.observe((activity as MainActivity), Observer { response ->
                if (response.isSuccessful) {
                    //clear/hide the error views
                    clearError()
                    //display the data
                    response.body()?.let { recipesAdapter.setData(it) }
                } else {
                    //if the response is not successful, show error
                    displayGenericError()
                }
            })
        } catch (e: Exception) {
            displayGenericError()
        } catch (e: NetworkErrorException) {
            displayNetworkError()
        }
    }

    //change colours of the selected button
    fun selectButton(buttonName: MaterialButton) {
        buttonName.setBackgroundColor(resources.getColor(R.color.teal))
        buttonName.setTextColor(resources.getColor(R.color.white))
        buttonName.setStrokeColorResource(R.color.teal)
    }

    //reset the button colours
    fun resetButton(buttonName: MaterialButton) {
        buttonName.setBackgroundColor(resources.getColor(R.color.white))
        buttonName.setTextColor(resources.getColor(R.color.black))
        buttonName.setStrokeColorResource(R.color.black)
    }

    //setup the random button
    fun setRandomButton() {
        selectButton(binding.randomButton)
        try {
            resetButton(selectedButton)
        } catch (e: UninitializedPropertyAccessException) {
            Log.d(TAG, e.toString())
        }
        getRandomRecipes()
        selectedButton = binding.randomButton
        button = "randomButton"
    }

    //setup the starters button
    fun setStartersButton() {
        selectButton(binding.startersButton)
        resetButton(selectedButton)
        getCourse("appetizer")
        selectedButton = binding.startersButton
        button = "startersButton"
    }

    //setup the mains button
    fun setMainsButton() {
        selectButton(binding.mainsButton)
        resetButton(selectedButton)
        getCourse("main course")
        selectedButton = binding.mainsButton
        button = "mainsButton"
    }

    //setup the desserts button
    fun setDessertsButton() {
        selectButton(binding.dessertsButton)
        resetButton(selectedButton)
        getCourse("dessert")
        selectedButton = binding.dessertsButton
        button = "dessertsButton"
    }
}