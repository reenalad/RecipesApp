package com.example.recipesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.models.Recipe
import com.example.recipesapp.models.Recipes
import com.example.recipesapp.ui.currentView
import kotlinx.android.synthetic.main.row_item.view.*

class RecipesAdapter(private val onItemClicked: (recipe: Recipe) -> Unit) :
    RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>() {

    companion object MyDiffUtil : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.id == newItem.id
        }
    }

    //list to hold the recipes
    var list = Recipes(arrayListOf())

    inner class RecipesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        //get the layout based on the current view
        if (currentView == "search") {
            return RecipesViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.grid_row_item, parent, false),
            )
        } else {
            return RecipesViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false),
            )
        }
    }

    override fun onBindViewHolder(holder: RecipesAdapter.RecipesViewHolder, position: Int) {
        //set the recipe data
        holder.itemView.title_text.text = list.recipes[position].title
        Glide.with(holder.itemView.context).load(list.recipes[position].image)
            .into(holder.itemView.recipe_image)
        holder.itemView.setOnClickListener {
            onItemClicked(list.recipes[position])
        }
    }

    override fun getItemCount(): Int {
        return list.recipes.count()
    }

    fun setData(newList: Recipes) {
        list = newList
        notifyDataSetChanged()
    }
}


