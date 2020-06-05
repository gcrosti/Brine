package com.gdc.recipebook.screens.meallist

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.RoomDatabase
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.R
import com.gdc.recipebook.database.FirebaseDataManager
import com.gdc.recipebook.database.MealRoomDatabase
import com.gdc.recipebook.database.SharedPrefsDataManager
import com.gdc.recipebook.databinding.FragmentMealListBinding
import kotlinx.android.synthetic.main.fragment_meal_list.*
import kotlinx.android.synthetic.main.view_meal_list_item.view.*
import kotlinx.android.synthetic.main.view_newmeal_dialog.*
import kotlinx.android.synthetic.main.view_welcome.*
import java.util.*

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
    private lateinit var sharedPrefsDataManager: SharedPrefsDataManager
    private lateinit var firebaseDataManager: FirebaseDataManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentMealListBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_meal_list,container,false)

        val application = requireNotNull(this.activity).application

        val dataSource = MealRoomDatabase.getInstance(application).databaseDAO
        val viewModelFactory = MealListViewModelFactory(dataSource,application)
        val mealListViewModel = viewModelFactory.create(MealListViewModel::class.java)

        binding.mealListViewModel = mealListViewModel

        val adapter = MealListAdapter()
        binding.recipeListRecyclerView.adapter = adapter
        adapter.submitList(mealListViewModel.meals)



        //OLD CODE

        sharedPrefsDataManager = context?.let {
            SharedPrefsDataManager(
                it
            )
        }!!
        firebaseDataManager = context?.let {
            FirebaseDataManager(
                it
            )
        }!!

        if (sharedPrefsDataManager.readList().isEmpty()) {
            return inflater.inflate(R.layout.view_welcome,container,false)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (welcomeButton == null) {

        fabRecipeList.setOnClickListener() {
            createRecipeDialog().show()
        }}
        else {
            welcomeButton.setOnClickListener() {
                createRecipeDialog().show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecipeListFragment.
         */
        // TODO: Rename and change types and number of parameters
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
        lateinit var mealList: MutableList<Meal>
        activity?.let{ it ->
            mealList = if (sharedPrefsDataManager.readList().isNotEmpty()) {
                sharedPrefsDataManager.readList()
            } else {
                mutableListOf()
            }
            dialog = Dialog(it)
            dialog.setContentView(R.layout.view_newmeal_dialog)
            dialog.createRecipeButton.setOnClickListener {
                val editText = dialog.recipeNameEditor.text.toString()
                if (mealList.any { it.name.toLowerCase(Locale.ROOT) == editText.toLowerCase(Locale.ROOT) }) {
                    val text = "Oops! It looks like a meal with that name already exists."
                    val duration = Toast.LENGTH_LONG
                    val toast = Toast.makeText(view?.context,text,duration)
                    toast.show()
                } else {
                    val newMeal =
                        Meal(editText)
                    mealList.add(newMeal)
                    sharedPrefsDataManager.saveList(mealList)
                    firebaseDataManager.saveMeal(newMeal)
                    navToEditor(editText)
                    dialog.dismiss()
                }

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