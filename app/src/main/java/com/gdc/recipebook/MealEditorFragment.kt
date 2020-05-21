package com.gdc.recipebook

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_meal_editor.*
import kotlinx.android.synthetic.main.fragment_meal_editor.view.*

class MealEditorFragment: Fragment() {
    lateinit var listDataManager: ListDataManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_meal_editor,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listDataManager = ListDataManager(view.context)
        val mealList = listDataManager.readList()
        lateinit var oldMeal: Meal
        arguments?.let {
            val oldRecipeName = MealEditorFragmentArgs.fromBundle(it).mealName
            oldMeal = listDataManager.readMeal(mealList,oldRecipeName)!!

        }
        val mealNameEditable = Editable.Factory.getInstance().newEditable(oldMeal.name)
        if (oldMeal.notes.isNotEmpty()) {
                view.editNotes.text = Editable.Factory.getInstance().newEditable(oldMeal.notes)
            }
        view.editName.text = mealNameEditable
        setChecks(oldMeal)

        saveRecipeButton.setOnClickListener {
            val newMeal = Meal(editName.text.toString())
            newMeal.notes = editNotes.text.toString()
            setFunction(newMeal)
            listDataManager.editMeal(mealList,oldMeal.name,newMeal)
            listDataManager.saveList(mealList)
            listDataManager.saveNewMealToDatabase(newMeal)
            navToMeal(newMeal.name)

        }

        deleteMeal.setOnClickListener {
            val alert = createDeleteDialog(mealList,oldMeal.name)
            alert.show()
        }
    }





    private fun setFunction(meal:Meal) {
        val funcList = mutableListOf<String>()
        if (proteinCheck.isChecked) {
            funcList.add(proteinCheck.text.toString())
        }
        if (vegetableCheck.isChecked) {
            funcList.add(vegetableCheck.text.toString())
        }
        if (starchCheck.isChecked) {
            funcList.add(starchCheck.text.toString())
        }
        if (ingredientCheck.isChecked) {
            funcList.add(ingredientCheck.text.toString())
        }
        if (dipCheck.isChecked) {
            funcList.add(dipCheck.text.toString())
        }
        if (dressingCheck.isChecked) {
            funcList.add(dressingCheck.text.toString())
        }
        if (dessertCheck.isChecked) {
            funcList.add(dessertCheck.text.toString())
        }
        if (beverageCheck.isChecked) {
            funcList.add(beverageCheck.text.toString())
        }
        meal.function = funcList.toString()
    }

    private fun setChecks(meal: Meal) {
        Log.d("checks run?","true")
        val checkBoxes = listOf<CheckBox>(proteinCheck,vegetableCheck,starchCheck,ingredientCheck,dipCheck,dressingCheck,dessertCheck,beverageCheck)
        if (meal.function.isNotEmpty()) {
            val funcList = listDataManager.convertStringToList(meal.function)
            for (checkBox in checkBoxes) {
                if (funcList.contains(checkBox.text)) {
                    checkBox.isChecked = true
                }
            }
        }
    }

    private fun createDeleteDialog(mealList: MutableList<Meal>,name:String): AlertDialog {
        lateinit var alertDialog: AlertDialog
        activity?.let {
            val alertDialogBuilder = AlertDialog.Builder(it)
            val listener = DialogInterface.OnClickListener {
                dialog, which ->  dialog.dismiss()
                listDataManager.deleteMeal(mealList,name)
                listDataManager.saveList(mealList)
                navToList()
            }
            alertDialogBuilder.setTitle("Are you sure we want to delete this meal?")
                .setPositiveButton("Yes",listener)
            alertDialog = alertDialogBuilder.create()
        }
        return alertDialog
    }

    private fun navToList() {
        val action = MealEditorFragmentDirections.actionRecipeEditorFragmentToRecipeListFragment()
        view?.findNavController()?.navigate(action)
    }
    private fun navToMeal(name:String) {
        val action = MealEditorFragmentDirections.actionRecipeEditorFragmentToRecipeFragment()
        action.mealName = name
        view?.findNavController()?.navigate(action)
    }


}