package com.gdc.recipebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
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
        val listDataManager = ListDataManager(view.context)
        arguments?.let {
            args = MealFragmentArgs.fromBundle(it)
        }
        recipeTitle.text = args.mealName
        val meal = listDataManager.readMeal(listDataManager.readList(),args.mealName)!!
        val funcNames = if (meal.function.isNotEmpty())
        {meal.function.substring(1,meal.function.length-1)} else {""}
        recipeFunction.text = funcNames
        recipeNotes.text = meal.notes

        fabEditRecipe.setOnClickListener {
            navToEditor(meal.name)
        }
    }



    private fun navToEditor(name:String) {
        val action = MealFragmentDirections.actionRecipeFragmentToRecipeEditorFragment()
        action.mealName = name
        view?.findNavController()?.navigate(action)
    }

    private fun navToMealList() {
        val action = MealFragmentDirections.actionRecipeFragmentToRecipeListFragment()
        view?.findNavController()?.navigate(action)
    }
}