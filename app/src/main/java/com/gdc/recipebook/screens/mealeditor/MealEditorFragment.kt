package com.gdc.recipebook.screens.mealeditor

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
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
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.databinding.FragmentMealEditorBinding


class MealEditorFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //CREATE BINDING OBJECT
        val binding: FragmentMealEditorBinding = DataBindingUtil.inflate(
            inflater,R.layout.fragment_meal_editor,container,false)

        val application = requireNotNull(this.activity).application

        //CREATE DATABASE REFERENCE
        val dataSource = MealRoomDatabase.getInstance(application).databaseDAO

        //RETRIEVE ARG DATA
        var nameFromArg = ""
        arguments?.let {
            nameFromArg = MealEditorFragmentArgs.fromBundle(it).mealName
        }

        //CREATE VIEWMODEL AND SET OBJECT RELATIONS
        val viewModelFactory = MealEditorViewModelFactory(dataSource,application,nameFromArg)

        val mealEditorViewModel = viewModelFactory.create(MealEditorViewModel::class.java)

        mealEditorViewModel.newMealId.observe(viewLifecycleOwner, Observer {
            mealEditorViewModel.setMealWithRelations(mealEditorViewModel.mealName)
        })

        //BUTTON CLICK HANDLERS
        mealEditorViewModel.onNewImageClick.observe(viewLifecycleOwner,  Observer {
            if (it == true) {
                val getContent =
                    registerForActivityResult(PhotoActivityResultContract()) { uri: Uri? ->
                        mealEditorViewModel.addNewImage(uri.toString())
                    }
                getContent.launch("image/*")
            }
        })

        mealEditorViewModel.onSaveMealClick.observe(viewLifecycleOwner,  Observer {
            if (it == true) {
                mealEditorViewModel.onSave()
                navToList()
            }
        })

        mealEditorViewModel.onDeleteMealClick.observe(viewLifecycleOwner,  Observer {
            if (it == true) {
                val alert = createDeleteDialog(mealEditorViewModel)
                alert.show()
            }
        })

        mealEditorViewModel.onAddResourcesClick.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                view?.context?.let { it1 -> mealEditorViewModel.addResource(it1) }
            }
        })

        binding.mealEditorViewModel = mealEditorViewModel
        binding.lifecycleOwner = this

        return binding.root
    }

 /*   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        addResourceButton.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(view.context.resources,
                R.drawable.common_full_open_on_phone
            )
            val pendingIntent = createPendingIntent(mealList,oldMeal)
            val uri = Uri.parse("https://www.google.com/")
            val intentBuilder = CustomTabsIntent.Builder()
            intentBuilder.addMenuItem("callback",pendingIntent)
            intentBuilder.setActionButton(bitmap,"callback",pendingIntent,true)
            context?.let { it1 -> intentBuilder.build().launchUrl(it1,uri) }
        }
        if (resourceList.isNullOrEmpty()) {
            resourcesRecyclerView.visibility = View.INVISIBLE
        } else {
            resourcesRecyclerView.apply {
                val listener = View.OnClickListener {
                    val url = it.resourceURL.text
                    resourceList.remove(url)
                }
                val rAdapter =
                    ResourceListAdapter(
                        resourceList,
                        listener
                    )
                adapter = rAdapter
                layoutManager = LinearLayoutManager(activity)
            }
        }


    }
*/

    private fun createDeleteDialog(viewModel:MealEditorViewModel): AlertDialog {
        lateinit var alertDialog: AlertDialog
        activity?.let {
            val alertDialogBuilder = AlertDialog.Builder(it)
            val listener = DialogInterface.OnClickListener {
                dialog, which ->  dialog.dismiss()
                viewModel.onDelete()
                navToList()
            }
            alertDialogBuilder.setTitle("Are you sure we want to delete this meal?")
                .setPositiveButton("Yes",listener)
            alertDialog = alertDialogBuilder.create()
        }
        return alertDialog
    }

    private fun navToList() {
        val action =
            MealEditorFragmentDirections.actionRecipeEditorFragmentToRecipeListFragment()
        view?.findNavController()?.navigate(action)
    }
    private fun navToMeal(name:String) {
        val action =
            MealEditorFragmentDirections.actionRecipeEditorFragmentToRecipeFragment()
        action.mealName = name
        view?.findNavController()?.navigate(action)
    }


}