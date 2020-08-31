package com.gdc.recipebook.screens.meallist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.gdc.recipebook.BrineApplication
import com.gdc.recipebook.R
import com.gdc.recipebook.databinding.FragmentMealListBinding
import com.gdc.recipebook.screens.modules.createDishDialog

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MealListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class MealListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do Nothing When Back Pressed on list screen
            }
        })

        val binding: FragmentMealListBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_meal_list,container,false)

        val repository = (requireContext().applicationContext as BrineApplication).repository

        val viewModelFactory = MealListViewModelFactory(repository)
        val mealListViewModel = viewModelFactory.create(MealListViewModel::class.java)

        binding.mealListViewModel = mealListViewModel
        repository.updateMealsWithFunctions()

        val adapter = MealListAdapter(MealListListener { name -> navToMeal(name) })

        repository.mealsWithFunctions.observe(viewLifecycleOwner, Observer {
           adapter.submitList(it)
        })

        binding.recipeListRecyclerView.adapter = adapter

        binding.lifecycleOwner = this


        mealListViewModel.showNewMealDialog.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                view?.let {view ->
                    createDishDialog(view,repository).show()
                }
            }

        })


        // Inflate the layout for this fragment
        return binding.root
    }



    private fun navToMeal(name:String) {
        val action =
            MealListFragmentDirections.actionRecipeListFragmentToRecipeFragment()
        action.mealName = name
        view?.findNavController()?.navigate(action)
    }
}
