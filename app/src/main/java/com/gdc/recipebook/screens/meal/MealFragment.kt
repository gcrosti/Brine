package com.gdc.recipebook.screens.meal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.gdc.recipebook.R
import com.gdc.recipebook.database.SharedPrefsDataManager
import kotlinx.android.synthetic.main.fragment_meal.*


class MealFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navToMealList()
            }
        })

        return inflater.inflate(R.layout.fragment_meal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lateinit var args: MealFragmentArgs
        val listDataManager =
            SharedPrefsDataManager(view.context)
        arguments?.let {
            args = MealFragmentArgs.fromBundle(it)
        }
        recipeTitle.text = args.mealName
        val meal = listDataManager.readMeal(listDataManager.readList(),args.mealName)!!
        val funcNames = if (meal.function.isNotEmpty())
        {meal.function.substring(1,meal.function.length-1)} else {""}
        recipeFunction.text = funcNames
        recipeNotes.text = meal.notes
        if (!meal.imageURI.isNullOrEmpty()) {
            displayImage(meal.imageURI)
            Log.d("Glide image uri for ${meal.name}", meal.imageURI)
        } else {recipeImage.visibility = View.GONE}

        fabEditRecipe.setOnClickListener {
            navToEditor(meal.name)
        }
    }



    private fun navToEditor(name:String) {
        val action =
            MealFragmentDirections.actionRecipeFragmentToRecipeEditorFragment()
        action.mealName = name
        view?.findNavController()?.navigate(action)
    }

    private fun navToMealList() {
        val action =
            MealFragmentDirections.actionRecipeFragmentToRecipeListFragment()
        view?.findNavController()?.navigate(action)
    }


    private fun displayImage(uriString: String) {
        view?.context?.let {
            Glide
                .with(it)
                .load(uriString)
                .fitCenter()
                .into(recipeImage)
        }
    }
}