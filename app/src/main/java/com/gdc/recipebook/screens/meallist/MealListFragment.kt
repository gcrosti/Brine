package com.gdc.recipebook.screens.meallist

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
import com.gdc.recipebook.R
import com.gdc.recipebook.database.Repository
import com.gdc.recipebook.databinding.FragmentMealListBinding
import kotlinx.android.synthetic.main.view_newmeal_dialog.*

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

        val binding: FragmentMealListBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_meal_list,container,false)

        val viewModelFactory = MealListViewModelFactory()
        val mealListViewModel = viewModelFactory.create(MealListViewModel::class.java)

        binding.mealListViewModel = mealListViewModel

        val adapter = MealListAdapter(MealListListener { name -> navToMeal(name) })

        Repository.mealsWithFunctions.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
                Log.d("mealswithfunc submitted",it.toString())})

        binding.recipeListRecyclerView.adapter = adapter

        binding.lifecycleOwner = this


        mealListViewModel.showNewMealDialog.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                    val dialog = createRecipeDialog()
                    dialog.show()
                }

        })


        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MealListFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MealListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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
        val action =
            MealListFragmentDirections.actionRecipeListFragmentToRecipeEditorFragment()
        action.mealName = name
        view?.findNavController()?.navigate(action)
    }

    private fun navToMeal(name:String) {
        val action =
            MealListFragmentDirections.actionRecipeListFragmentToRecipeFragment()
        action.mealName = name
        Log.d("navigating to", name)
        view?.findNavController()?.navigate(action)
    }
}
