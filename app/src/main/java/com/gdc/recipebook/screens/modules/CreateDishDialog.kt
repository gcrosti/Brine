package com.gdc.recipebook.screens.modules

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import com.gdc.recipebook.R
import com.gdc.recipebook.database.interfaces.RepositoryInterface
import kotlinx.android.synthetic.main.view_newmeal_dialog.*

fun createDishDialog(view: View, repository: RepositoryInterface? = null): Dialog {
    val navController = Navigation.findNavController(view)
    lateinit var dialog: Dialog
    view.context?.let {
        dialog = Dialog(it)
        dialog.setContentView(R.layout.view_newmeal_dialog)
        dialog.createRecipeButton.setOnClickListener {
            val editText = dialog.recipeNameEditor.text.toString()
            var nameAccepted = false

            if (editText.isBlank()) {
                val toast = Toast.makeText(view.context,R.string.dishNameBlankText,Toast.LENGTH_LONG)
                toast.show()
            } else {
                nameAccepted = true
                repository?.let { repo ->
                    if(repo.isMealNameTaken(editText)) {
                        val toast = Toast.makeText(view.context,R.string.dishNameTakenText,Toast.LENGTH_LONG)
                        toast.show()
                        nameAccepted = false
                    }
                }
            }

            if (nameAccepted) {
                val bundle = Bundle()
                bundle.putString("mealName",editText)
                navController.navigate(R.id.action_global_recipeEditorFragment,bundle)
                dialog.dismiss()
            }
        }
    }
    return dialog
}