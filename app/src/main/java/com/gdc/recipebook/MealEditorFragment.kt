package com.gdc.recipebook

import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_meal_editor.*
import kotlinx.android.synthetic.main.fragment_meal_editor.view.*

class MealEditorFragment: Fragment() {
    lateinit var sharedPrefsDataManager: SharedPrefsDataManager
    lateinit var firebaseDataManager: FirebaseDataManager



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_meal_editor,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefsDataManager = SharedPrefsDataManager(view.context)
        firebaseDataManager = FirebaseDataManager(view.context)
        val mealList = sharedPrefsDataManager.readList()
        lateinit var oldMeal: Meal
        val newMeal = Meal("placeHolder")
        arguments?.let {
            val oldRecipeName = MealEditorFragmentArgs.fromBundle(it).mealName
            oldMeal = sharedPrefsDataManager.readMeal(mealList,oldRecipeName)!!

        }
        val mealNameEditable = Editable.Factory.getInstance().newEditable(oldMeal.name)
        if (oldMeal.notes.isNotEmpty()) {
                view.editNotes.text = Editable.Factory.getInstance().newEditable(oldMeal.notes)
            }
        if (oldMeal.imageURI.isNotEmpty()) {
            view.imageURI.text = oldMeal.imageURI
        }
        view.editName.text = mealNameEditable
        setChecks(oldMeal)

        imageButton.setOnClickListener {
            val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
                uri: Uri? -> view.imageURI.text = uri.toString()
            }
            getContent.launch("image/*")
        }

        saveRecipeButton.setOnClickListener {
            newMeal.name = editName.text.toString()
            newMeal.notes = editNotes.text.toString()
            newMeal.imageURI = view.imageURI.text.toString()
            setFunction(newMeal)
            sharedPrefsDataManager.editMeal(mealList,oldMeal.name,newMeal)
            sharedPrefsDataManager.saveList(mealList)
            firebaseDataManager.saveMeal(newMeal)
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
            val funcList = sharedPrefsDataManager.convertStringToList(meal.function)
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
                firebaseDataManager.deleteMeal(mealList.filter { it.name == name }[0])
                sharedPrefsDataManager.deleteMeal(mealList,name)
                sharedPrefsDataManager.saveList(mealList)
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