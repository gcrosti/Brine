package com.gdc.recipebook.screens.mealeditor

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.gdc.recipebook.MainActivity
import com.gdc.recipebook.R
import com.gdc.recipebook.database.MealRoomDatabase
import com.gdc.recipebook.databinding.FragmentMealEditorBinding
import com.gdc.recipebook.screens.mealeditor.resources.ResourceBroadcastReceiver
import com.gdc.recipebook.screens.mealeditor.resources.ResourceListAdapter
import com.gdc.recipebook.screens.mealeditor.resources.ResourceListListener
import com.gdc.recipebook.screens.mealeditor.utils.EditorLifecycleObserver
import com.gdc.recipebook.screens.mealeditor.utils.PhotoActivityResultContract
import com.gdc.recipebook.screens.mealeditor.viewModel.MealEditorViewModel
import com.gdc.recipebook.screens.mealeditor.viewModel.MealEditorViewModelFactory


class MealEditorFragment: Fragment() {

    //CREATE VIEWMODEL AND SET OBJECT RELATIONS
    val viewModelFactory =
        MealEditorViewModelFactory()
    val mealEditorViewModel = viewModelFactory.create(MealEditorViewModel::class.java)

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
            mealEditorViewModel.mealName = nameFromArg
        }

        mealEditorViewModel.setDatabase(dataSource)
        mealEditorViewModel.setMealId()


        //BUTTON CLICK HANDLERS
        mealEditorViewModel.onNewImageClick.observe(viewLifecycleOwner,  Observer {
            if (it == true) {
                val getContent =
                    registerForActivityResult(PhotoActivityResultContract()) { uri: Uri? ->
                        mealEditorViewModel.addNewImageURL(uri.toString())
                    }
                getContent.launch("image/*")
            }
        })

        mealEditorViewModel.onSaveMealClick.observe(viewLifecycleOwner,  Observer {
            if (it == true) {
                mealEditorViewModel.onSave()
                navToMeal(mealEditorViewModel.mealName)
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
                context?.let { context -> launchCustomTab(context) }
            }
        })


        //EDITABLE OBSERVERS
        binding.editName.addTextChangedListener(mealEditorViewModel.nameTextWatcher)
        binding.editNotes.addTextChangedListener(mealEditorViewModel.notesTextWatcher)
        binding.imageURI.addTextChangedListener(mealEditorViewModel.imageTextWatcher)

        binding.mealEditorViewModel = mealEditorViewModel
        binding.lifecycleOwner = this

        //RESOURCES LOGIC
        Log.d("adapter creation", "happening now")
        val adapter = ResourceListAdapter(ResourceListListener { resource -> mealEditorViewModel.removeResource(resource) })
        binding.resourcesRecyclerView.adapter = adapter

        val getUrlFromIntent: () -> Unit = {
            val urlString = activity?.intent?.extras?.getString(Intent.EXTRA_TEXT)
            urlString?.let {
                mealEditorViewModel.addNewResource(it)
                adapter.notifyDataSetChanged()
            }
        }

        activity?.lifecycle?.let {
            val lifecycleObserver = EditorLifecycleObserver(it,getUrlFromIntent)
            it.addObserver(lifecycleObserver)
        }



        mealEditorViewModel.resources.observe(viewLifecycleOwner, Observer {
            it.toString()
            it?.let {
                Log.d("list submitted",it.toString())
                adapter.submitList(it)
            }
        })


        return binding.root
    }


    private fun createDeleteDialog(viewModel: MealEditorViewModel): AlertDialog {
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



    private fun launchCustomTab(context: Context) {
        val bitmap = BitmapFactory.decodeResource(context.resources,
            R.drawable.common_full_open_on_phone
        )
        val pendingIntent = createPendingIntent(context)
        val uri = Uri.parse("https://www.google.com/")
        val intentBuilder = CustomTabsIntent.Builder()
        intentBuilder.addMenuItem("callback",pendingIntent)
        intentBuilder.setActionButton(bitmap,"callback",pendingIntent,true)
        context.let { it ->
            val customTabsIntent = intentBuilder.build()
            customTabsIntent.intent.setPackage("com.android.chrome")
            customTabsIntent.launchUrl(it,uri)}
    }

    private fun createPendingIntent(context: Context): PendingIntent {
        val resourceBroadcastReceiver = ResourceBroadcastReceiver()
        val intent = Intent(context,resourceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_ONE_SHOT)
    }
}