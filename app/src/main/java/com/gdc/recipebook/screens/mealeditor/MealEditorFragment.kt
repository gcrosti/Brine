package com.gdc.recipebook.screens.mealeditor

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
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
import com.gdc.recipebook.MainActivity
import com.gdc.recipebook.R
import com.gdc.recipebook.database.MealRoomDatabase
import com.gdc.recipebook.databinding.FragmentMealEditorBinding


class MealEditorFragment: Fragment() {

    //CREATE VIEWMODEL AND SET OBJECT RELATIONS
    val viewModelFactory = MealEditorViewModelFactory()
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


        //EDITABLE OBSERVERS
        binding.editName.addTextChangedListener(mealEditorViewModel.nameTextWatcher)
        binding.editNotes.addTextChangedListener(mealEditorViewModel.notesTextWatcher)
        binding.imageURI.addTextChangedListener(mealEditorViewModel.imageTextWatcher)

        binding.mealEditorViewModel = mealEditorViewModel
        binding.lifecycleOwner = this


        return binding.root
    }
/*
    override fun onResume() {
        super.onResume()
        val urlstring = arguments?.getString(ARG_NAME)
        var listOfResources = mutableListOf<Resource>()
        if (mealEditorViewModel.resources.isNotEmpty()) {
                listOfResources = mealEditorViewModel.resources
            }
        urlstring?.let {
            mealEditorViewModel.addNewResource(it)
            listOfResources.add(Resource(resourceURL = it))
            mealEditorViewModel.adapter.submitList(listOfResources)
            mealEditorViewModel.adapter.notifyItemInserted(listOfResources.size -1)
            Log.d("ORresourceUri",listOfResources.toString())
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

    class ResourceBroadcastReceiver(): BroadcastReceiver() {

        val KEY_ACTION_SOURCE = "brineactiontaken"
        override fun onReceive(context: Context?, intent: Intent?) {
            val uriText = intent?.data.toString()
            Log.d(KEY_ACTION_SOURCE,uriText)
            val onReceiveIntent = Intent(context, MainActivity::class.java)
            onReceiveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            onReceiveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            onReceiveIntent.putExtra(Intent.EXTRA_TEXT,uriText)
            context?.startActivity(onReceiveIntent)
        }
    }
/*
    private fun launchCustomTab(context: Context) {
        val bitmap = BitmapFactory.decodeResource(context.resources,
            R.drawable.common_full_open_on_phone
        )
        val pendingIntent = createPendingIntent(context)
        val uri = Uri.parse("https://www.google.com/")
        val intentBuilder = CustomTabsIntent.Builder()
        intentBuilder.addMenuItem("callback",pendingIntent)
        intentBuilder.setActionButton(bitmap,"callback",pendingIntent,true)
        context?.let { it ->
            val customTabsIntent = intentBuilder.build()
            customTabsIntent.intent.setPackage("com.android.chrome")
            customTabsIntent.launchUrl(it,uri)}
    }

    private fun createPendingIntent(context: Context): PendingIntent {
        val resourceBroadcastReceiver = ResourceBroadcastReceiver()
        val intent = Intent(context,resourceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_ONE_SHOT)
    }


    companion object {
        const val ARG_NAME = "newResourceUrl"

        fun newInstance(name:String): MealEditorFragment {
            val fragment = MealEditorFragment()
            val bundle = Bundle().apply {
                putString(ARG_NAME,name)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
*/

}