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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.databinding.FragmentDetailsBinding
import com.example.recipesapp.models.Recipe
import com.example.recipesapp.utils.Constants
import com.example.recipesapp.viewmodel.FavouritesViewModel
import com.example.recipesapp.viewmodel.RecipesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_details.view.*
import kotlinx.coroutines.runBlocking

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    //variables for recipeId and if the recipe is a favourite
    var recipeId = 0
    var isRecipeFavourite = false

    //access the viewmodels
    val viewModel: RecipesViewModel by activityViewModels()
    val favouritesViewModel: FavouritesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //hide the bottom navigation
        val navBar: BottomNavigationView? = getActivity()?.findViewById(R.id.nav_view)
        if (navBar != null) {
            navBar.visibility = View.GONE
        }

        //make tha back arrow visible
        val backArrow = (activity as MainActivity).toolbar.back_arrow
        backArrow.visibility = View.VISIBLE

        //when the back arrow is clicked, go back to the last page
        backArrow.setOnClickListener {
            findNavController().navigateUp()
            backArrow.visibility = View.INVISIBLE
        }

        //get the recipeId from the previous page
        arguments?.let { viewModel.getRecipeById(it.getInt("recipeId"), Constants.API_KEY) }


        try {
            //get the recipe details
            viewModel.individualRecipe.observe((activity as MainActivity), Observer { response ->
                if (response.isSuccessful) {
                    //set the recipeId value with that of the current recipe
                    recipeId = response.body()!!.id
                    //check if this recipe is in the favourite recipes database
                    runBlocking {
                        isRecipeFavourite = favouritesViewModel.checkRecipeById(recipeId)
                    }
                    //display the recipe details
                    response.body()?.let { setData(it, isRecipeFavourite) }

                } else {
                    displayGenericError()
                    Log.d(TAG, "Error: failed with code " + response.code().toString())
                }
            })
        } catch (e: Exception) {
            displayGenericError()
        } catch (e: NetworkErrorException) {
            displayNetworkError()
        }

        //if the favourites icon is clicked add/delete from favourites database
        binding.favouritesIcon.setOnClickListener {
            try {
                //get the current recipe
                val currentRecipe = viewModel.individualRecipe.value?.body()!!
                //if the recipe is in favourites, deselect the icon
                if (isRecipeFavourite) {
                    it.favourites_icon.setImageResource(R.drawable.favourite_border_icon)
                    it.favourites_icon.setTag(R.drawable.favourite_border_icon)
                    //change the text
                    binding.favourites.text = "Add to Favourites"
                    //delete the recipe from the database
                    runBlocking {
                        favouritesViewModel.delete(currentRecipe)
                    }
                } else {
                    //if the recipe wasn't in the favourites database, change the icon
                    it.favourites_icon.setImageResource(R.drawable.favourite_icon)
                    it.favourites_icon.setTag(R.drawable.favourite_icon)
                    //change the text
                    binding.favourites.text = "Favourite"
                    //insert the recipe into the database
                    favouritesViewModel.insertRecipe(currentRecipe)
                }
            } catch (e: NullPointerException) {
                Log.d("Details Fragment NullPointerException: ", e.toString())
            }
        }
    }

    private fun setData(data: Recipe, isFavourite: Boolean) {
        //load the image into the imageview
        Glide.with(binding.recipeImage).load(data.image).into(binding.recipeImage)

        //set the icons and text
        binding.servingsIcon.setImageResource(R.drawable.cutlery)
        binding.servings.text = "Serves " + data.servings.toString()
        binding.timeIcon.setImageResource(R.drawable.time)

        //convert time into hours and minutes
        val time = data.readyInMinutes
        val hours = time / 60
        val hoursToDisplay: String =
            if (hours > 1) hours.toString() + "hrs" else if (time == 1) time.toString() + "hr" else ""
        val minutes = time % 60
        val minsToDisplay: String
        if (minutes > 0) minsToDisplay = minutes.toString() + "mins" else minsToDisplay = ""
        binding.time.text = hoursToDisplay + minsToDisplay

        //if the recipe is a favourite, display red heart icon and correct text
        if (isFavourite) {
            binding.favouritesIcon.setImageResource(R.drawable.favourite_icon)
            binding.favourites.text = "Favourite"
        } else {
            //if not a favourite, show alternative icon and text
            binding.favouritesIcon.setImageResource(R.drawable.favourite_border_icon)
            binding.favourites.text = "Add to Favourites"
        }

        //set the title
        binding.recipeTitle.text = data.title
        //remove the tags in the summary string and display the text
        val summary = data.summary.replace("<.*?>".toRegex(), "")
        binding.recipeSummary.text = summary

        binding.ingredientsTitle.text = "Ingredients"

        val ingredientsList = StringBuilder()
        var i = 0
        //format the ingredients list
        for (ingredient in data.extendedIngredients) {
            var item = data.extendedIngredients[i].original
            if (item[0] in 'a'..'z') {
                item = item.replaceFirstChar { it.uppercase() }
            }
            if (item.contains("T ")) {
                item = item.replace("T", "Tbsp")
            }
            if (item.contains("[0-9]{1,3}+t".toRegex())) {
                item = item.replace("([0-9]{1,3})t".toRegex(), "$1tsp")
            }
            ingredientsList.append(item + "\n\n")
            i++
        }
        binding.ingredients.text = ingredientsList.toString()

        binding.methodTitle.text = "Method"
        val analyzedInstructions = data.analyzedInstructions
        val instructions = StringBuilder()
        var stepNumber = 1
        //format the instruction steps
        for (instruction in analyzedInstructions) {
            for (step in instruction.steps) {
                instructions.append("Step " + stepNumber.toString() + ". " + step.step + "\n\n")
                stepNumber++
            }
        }
        //display the formatted instructions
        binding.method.text = instructions.toString()
    }

    //show the generic error image and message
    fun displayGenericError() {
        binding.errorImage.visibility = View.VISIBLE
        binding.errorImage.setImageResource(R.drawable.ic_baseline_broken_image_24)
        binding.errorText.text = "Unable to display data"
    }

    //show the network error and image
    fun displayNetworkError() {
        binding.errorImage.visibility = View.VISIBLE
        binding.errorImage.setImageResource(R.drawable.no_network)
        binding.errorText.text = "No network connection"
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).toolbar.back_arrow.visibility = View.INVISIBLE
    }
}


