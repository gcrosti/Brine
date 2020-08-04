package com.gdc.recipebook.screens.welcome

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.gdc.recipebook.BrineApplication
import com.gdc.recipebook.R
import com.gdc.recipebook.databinding.FragmentWelcomeBinding
import com.gdc.recipebook.screens.modules.createDishDialog
import kotlinx.android.synthetic.main.view_newmeal_dialog.*

class WelcomeFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val repository = (requireContext().applicationContext as BrineApplication).repository

        val binding: FragmentWelcomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_welcome,container,false)

        val viewModelFactory = WelcomeViewModelFactory()
        val welcomeViewModel = viewModelFactory.create(WelcomeViewModel::class.java)


        binding.welcomeViewModel = welcomeViewModel
        binding.lifecycleOwner = this

        repository.mealsWithFunctions.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                navToList()
            }
        })

        welcomeViewModel.onNewMealClick.observe(viewLifecycleOwner, Observer {
            if (it) {
                Log.d("newmealclick","welcome view")
                view?.let {view ->
                    createDishDialog(view).show()
                }
            }
        })

        return binding.root
    }

    private fun navToList() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToRecipeListFragment()
        view?.findNavController()?.navigate(action)
    }
}