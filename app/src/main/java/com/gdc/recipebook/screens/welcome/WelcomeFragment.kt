package com.gdc.recipebook.screens.welcome

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.gdc.recipebook.R
import com.gdc.recipebook.database.Repository
import com.gdc.recipebook.databinding.FragmentWelcomeBinding
import kotlinx.android.synthetic.main.view_newmeal_dialog.*

class WelcomeFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentWelcomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_welcome,container,false)

        val viewModelFactory = WelcomeViewModelFactory()
        val welcomeViewModel = viewModelFactory.create(WelcomeViewModel::class.java)

        binding.welcomeViewModel = welcomeViewModel
        binding.lifecycleOwner = this

        Repository.mealsWithFunctions.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                navToList()
            }
        })

        welcomeViewModel.onNewMealClick.observe(viewLifecycleOwner, Observer {
            if (it) {
                createRecipeDialog().show()
            }
        })

        return binding.root
    }

    private fun createRecipeDialog(): Dialog {
        lateinit var dialog: Dialog
        this.context?.let {
            dialog = Dialog(it)
            dialog.setContentView(R.layout.view_newmeal_dialog)
            dialog.createRecipeButton.setOnClickListener {
                val editText = dialog.recipeNameEditor.text.toString()
                navToEditor(editText)
                dialog.dismiss()
            }
        }
        return dialog
    }

    private fun navToEditor(name:String) {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToRecipeEditorFragment()
        action.mealName = name
        view?.findNavController()?.navigate(action)
    }

    private fun navToList() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToRecipeListFragment()
        view?.findNavController()?.navigate(action)
    }
}