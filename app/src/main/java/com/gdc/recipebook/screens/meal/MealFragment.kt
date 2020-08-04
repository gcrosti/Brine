package com.gdc.recipebook.screens.meal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.gdc.recipebook.BrineApplication
import com.gdc.recipebook.R
import com.gdc.recipebook.database.MealRoomDatabase
import com.gdc.recipebook.databinding.FragmentMealBinding
import com.gdc.recipebook.screens.meal.images.ImageSliderAdapter
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

        val binding: FragmentMealBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_meal,container,false)

        val viewModelFactory = MealViewModelFactory((requireContext().applicationContext as BrineApplication).repository)
        val mealViewModel = viewModelFactory.create(MealViewModel::class.java)

        binding.mealViewModel = mealViewModel
        binding.lifecycleOwner = this

        var nameFromArg = ""
        arguments?.let {
            nameFromArg = MealFragmentArgs.fromBundle(it).mealName
            mealViewModel.setNameFromArg(nameFromArg)
        }

        //IMAGES
        mealViewModel.images.observe(viewLifecycleOwner, Observer { imageList ->

                Log.d("imagelistsubmitted",imageList.toString())
                val adapter = context?.let { ImageSliderAdapter(it,imageList) }
                binding.imageViewPager.adapter = adapter

        })


        //BIND ADAPTER
        binding.resourcesMealRecyclerView.adapter = mealViewModel.adapter

        //EDIT MEAL OBSERVER
        mealViewModel.onEditMealClick.observe(viewLifecycleOwner, Observer {
            if (it) {
                mealViewModel.thisMeal.value?.name?.let { it1 -> navToEditor(it1) }
                mealViewModel.onNavigatingToEditMeal()
            }
        })

        return binding.root
    }

    private fun navToEditor(name:String) {
        val action =
            MealFragmentDirections.actionRecipeFragmentToRecipeEditorFragment()
        action.mealName = name
        view?.findNavController()?.navigate(action)
        Log.d("navfrommeal", "to editor")
    }

    private fun navToMealList() {
        val action =
            MealFragmentDirections.actionRecipeFragmentToRecipeListFragment()
        view?.findNavController()?.navigate(action)
    }


}