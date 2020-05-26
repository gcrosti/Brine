package com.gdc.recipebook

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.saurabharora.customtabs.CustomTabActivityHelper
import com.saurabharora.customtabs.extensions.launchWithFallback
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
        if (!oldMeal.imageURI.isNullOrEmpty()) {
            view.imageURI.text = Editable.Factory.getInstance().newEditable(oldMeal.imageURI)
            view.imageButton.text = "edit photo"
        }
        view.editName.text = mealNameEditable
        setChecks(oldMeal)

        imageButton.setOnClickListener {
            val getContent = registerForActivityResult(PhotoActivityResultContract()) {
                uri: Uri? -> view.imageURI.text = Editable.Factory.getInstance().newEditable(uri.toString())
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

        addResourceButton.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(view.context.resources,R.drawable.common_google_signin_btn_icon_dark)
            val requestCode = 100
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,"https://www.facebook.com/")
            //val pendingIntent = PendingIntent.getActivity(view.context,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            val broadcastIntent = Intent(view.context,ResourceBroadcastReceiver().javaClass)

            val pendingIntent = PendingIntent.getBroadcast(view.context,requestCode,broadcastIntent,PendingIntent.FLAG_ONE_SHOT)

            val uri = Uri.parse("https://www.google.com/")
            val customTabActivityHelper = CustomTabActivityHelper(context = view.context, lifecycle = lifecycle)
            customTabActivityHelper.mayLaunchUrl(uri)

            val customTabsIntent = CustomTabsIntent.Builder(customTabActivityHelper.session)
                //.setToolbarColor(ContextCompat.getColor(view.context,R.color.colorAccent))
                .setActionButton(bitmap,"Share Link",pendingIntent,true)
                .build()
            this.activity?.let { it1 -> customTabsIntent.launchWithFallback(activity = it1,uri = uri) }
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

    private fun selectResource() {


    }
}