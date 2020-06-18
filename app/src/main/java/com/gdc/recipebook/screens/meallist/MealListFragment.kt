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
import com.gdc.recipebook.database.MealRoomDatabase
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

        Log.d("Fragment","MealListFragment")

        val binding: FragmentMealListBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_meal_list,container,false)

        val application = requireNotNull(this.activity).application

        val dataSource = MealRoomDatabase.getInstance(application).databaseDAO
        val viewModelFactory = MealListViewModelFactory(dataSource,application)
        val mealListViewModel = viewModelFactory.create(MealListViewModel::class.java)

        binding.mealListViewModel = mealListViewModel

        val adapter = MealListAdapter()
        adapter.submitList(mealListViewModel.meals.value)

        binding.recipeListRecyclerView.adapter = adapter

        mealListViewModel.meals.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(mealListViewModel.meals.value)
                Log.d("list submitted","to meallist adapter")
            }
        })

        binding.lifecycleOwner = this


        mealListViewModel.showNewMealDialog.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                    val dialog = createRecipeDialog()
                    dialog.show()
                }

        })


        //mealListViewModel.meals.observe(viewLifecycleOwner, Observer {
        //    if (it.isEmpty()) {
        //        welcomeButton.setOnClickListener() {
        //            createRecipeDialog(mealListViewModel).show()
        //        }
//
        //        //return inflater.inflate(R.layout.view_welcome,container,false)
        //    }
       // })

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
}
