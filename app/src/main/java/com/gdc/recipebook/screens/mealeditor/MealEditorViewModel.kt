package com.gdc.recipebook.screens.mealeditor

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdc.recipebook.MainActivity
import com.gdc.recipebook.R
import com.gdc.recipebook.database.Repository
import com.gdc.recipebook.database.RoomDatabaseDAO
import com.gdc.recipebook.database.dataclasses.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MealEditorViewModel(var mealName: String, dataSource: RoomDatabaseDAO,application: Application): ViewModel() {

    //BOILERPLATE
    val database = dataSource
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val mealRepository = Repository(dataSource)




    //CLICKLISTENERS
    private var _onNewImageClick = MutableLiveData(false)
    val onNewImageClick: LiveData<Boolean>
        get() = _onNewImageClick

    private var _onSaveMealClick = MutableLiveData(false)
    val onSaveMealClick: LiveData<Boolean>
        get() = _onSaveMealClick

    private var _onDeleteMealClick = MutableLiveData(false)
    val onDeleteMealClick: LiveData<Boolean>
        get() = _onDeleteMealClick

    private var _onAddResourcesClick = MutableLiveData(false)
    val onAddResourcesClick: LiveData<Boolean>
        get() = _onAddResourcesClick

    //DATA FROM ARGS OR ACTIVITIES
    private var _newMealId = MutableLiveData(0L)
    val newMealId: LiveData<Long>
        get() = _newMealId

    private var _imageURI = MutableLiveData("")
    val imageURI: LiveData<String>
        get() = _imageURI

    private var _ResourceURI = MutableLiveData("")
    val resourceURI: LiveData<String>
        get() = _ResourceURI



    //LIVE DATA TO BE SAVED
    private var _mealWithRelations = MutableLiveData<MealWithRelations>()

    val mealFunctions: LiveData<MealFunction>
        get() = _mealWithRelations.value!!.functions

    val meal: LiveData<Meal>
        get() = _mealWithRelations.value!!.meal

    val mealResources: LiveData<MutableList<Resource>>
        get() = _mealWithRelations.value!!.resources

    init {
        setNewMealId(mealName)
        val meal = MutableLiveData(Meal(name = mealName))
        val functions = MutableLiveData(MealFunction(functionMealId = 0L))
        _mealWithRelations.value = MealWithRelations(meal = meal, functions = functions)
    }

    fun setMealWithRelations(name:String) {
        val newMeal = MutableLiveData(Meal(mealId = _newMealId.value!!, name = name))
        _mealWithRelations.value = MealWithRelations(newMeal)

    }

    private fun setNewMealId(name: String) {
        uiScope.launch {
            _newMealId.value = mealRepository.setMeal(name)
        }
    }


    //ADD NEW IMAGE WHEN USER SELECTS LOGIC
    fun addNewImage(url: String) {
        _imageURI.value = url
        val newImage = Image(imageMealId = 0L, imageURL = url)
        _mealWithRelations.value!!.images.add(newImage)
    }

    fun onNewImageClick() {
        _onNewImageClick.value = true
    }



    //SAVE DATA LOGIC
    fun onSaveMealClick() {
        _onSaveMealClick.value = true
    }

    fun onSave() {
        uiScope.launch {
            if (_mealWithRelations.value!!.images.isNotEmpty()) {
                for (image in _mealWithRelations.value!!.images) {
                    image.imageMealId = _newMealId.value!!
                }
            }

            if (_mealWithRelations.value!!.resources.value!!.isNotEmpty()) {
                for (resource in _mealWithRelations.value!!.resources.value!!) {
                    resource.resourceMealId = _newMealId.value!!
                }
            }
            mealRepository.saveMealWithRelations(_mealWithRelations.value!!)
            Log.d("saved data",_mealWithRelations.value.toString())
        }
    }


    //DELETE DATA LOGIC
    fun onDeleteMealClick() {
        _onDeleteMealClick.value = true
    }

    fun onDelete() {
        uiScope.launch {
            mealRepository.deleteMealWithRelations(_mealWithRelations.value!!)
        }
    }


    //MEAL RESOURCES LOGIG
    fun onAddResourceClick() {
        _onAddResourcesClick.value = true
    }

    fun onResourceAdded() {
        _onAddResourcesClick.value = false
    }

    fun addResource(context: Context) {
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
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            customTabsIntent.launchUrl(it,uri)}
    }


    private fun createPendingIntent(context: Context): PendingIntent {
        val resourceBroadcastReceiver = MyBroadcastReceiver()
        val intent = Intent(context,resourceBroadcastReceiver::class.java)
        Log.d("intent data", intent.data.toString())
        return PendingIntent.getBroadcast(context,0,intent,0)
    }



    //MEAL FUNCTIONS LOGIC
    fun onStarchClick() {
        _mealWithRelations.value?.functions?.value?.let {
            it.starch  = flipBoolean(it.starch)
        }
    }

    fun onVegClick() {
        _mealWithRelations.value?.functions?.value?.let {
            it.veg  = flipBoolean(it.veg)
        }
    }

    fun onBeverageClick() {
        _mealWithRelations.value?.functions?.value?.let {
            it.beverage  = flipBoolean(it.beverage)
        }
    }

    fun onProteinClick() {
        _mealWithRelations.value?.functions?.value?.let {
            it.protein  = flipBoolean(it.protein)
        }
    }

    fun onDessertClick() {
        _mealWithRelations.value?.functions?.value?.let {
            it.dessert  = flipBoolean(it.dessert)
        }
    }

    fun onIngredientClick() {
        _mealWithRelations.value?.functions?.value?.let {
            it.ingredient  = flipBoolean(it.ingredient)
        }
    }

    fun onDipClick() {
        _mealWithRelations.value?.functions?.value?.let {
            it.dip  = flipBoolean(it.dip)
        }
    }

    fun onDressingClick() {
        _mealWithRelations.value?.functions?.value?.let {
            it.dressing  = flipBoolean(it.dressing)
        }
    }



    //CANCEL JOB
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}

class MyBroadcastReceiver: ResourceBroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        val onReceiveIntent = Intent(context,MainActivity::class.java)
        onReceiveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        onReceiveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(onReceiveIntent)
        if (intent != null) {
            Log.d("received data", intent.data.toString())
        }
    }
}