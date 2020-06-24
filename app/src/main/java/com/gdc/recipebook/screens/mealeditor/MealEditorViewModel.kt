package com.gdc.recipebook.screens.mealeditor

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.gdc.recipebook.database.Repository
import com.gdc.recipebook.database.RoomDatabaseDAO
import com.gdc.recipebook.database.dataclasses.*
import com.gdc.recipebook.screens.mealeditor.resources.ResourceListAdapter
import com.gdc.recipebook.screens.mealeditor.resources.ResourceListListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MealEditorViewModel(): ViewModel() {

    //SET THREAD SCOPE
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //RETRIEVE DATABASE
    private lateinit var database: RoomDatabaseDAO
    private lateinit var mealRepository: Repository

    fun setDatabase(dataSource: RoomDatabaseDAO) {
        database = dataSource
        mealRepository = Repository(dataSource)
    }

    //CREATE A NEW MEALID FOR THIS MEAL
    private var newMealId = 0L
    public var test = ""

    fun createNewMealId() {
        uiScope.launch {
            newMealId = mealRepository.setMealId(mealName) }
            }

    //MEAL NAME LOGIC
    var mealName = ""

    val nameTextWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mealName = s.toString() } }

    //MEAL NOTES LOGIC
    var mealNotes = ""

    val notesTextWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mealNotes = s.toString() } }

    //MEAL LOGIC
    var thisMeal: Meal? = null

    //IMAGE LOGIC
    var imageURL = MutableLiveData("")

    val imageTextWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            imageURL.value = s.toString() } }


    private var _onNewImageClick = MutableLiveData(false)
    val onNewImageClick: LiveData<Boolean>
        get() = _onNewImageClick

    fun onNewImageClick() {
        _onNewImageClick.value = true
    }

    fun addNewImageURL(url: String) {
        imageURL.value = url
    }


/*
    //MEAL RESOURCES LOGIC
      private var _onAddResourcesClick = MutableLiveData(false)
    val onAddResourcesClick: LiveData<Boolean>
        get() = _onAddResourcesClick

     private var _resourceURI: MutableLiveData<String?> = MutableLiveData(null)
    val resourceURI: LiveData<String?>
        get() = _resourceURI

    val adapter = ResourceListAdapter(ResourceListListener { resource -> removeResource(resource) })

    private var _resources = mutableListOf<Resource>()
    val resources
            get() = _resources

    fun onAddResourceClick() {
        _onAddResourcesClick.value = true
    }

    fun addNewResource(uri: String) {
        Log.d("newresourcefound", uri)
        _resources.add(Resource(resourceURL = uri))
    }
*/

    //SAVE DATA LOGIC

    private var _onSaveMealClick = MutableLiveData(false)
    val onSaveMealClick: LiveData<Boolean>
        get() = _onSaveMealClick

    fun onSaveMealClick() {
        _onSaveMealClick.value = true
    }

    fun onSave() {
        uiScope.launch {
            if (thisMeal == null) {
                thisMeal = Meal(
                    mealId = newMealId,
                    name = mealName,
                    notes = mealNotes)
                Log.d("meal to be saved",thisMeal.toString())
            }
            val newImages = mutableListOf<Image>()

            imageURL.value?.let {
                if (it.isNotBlank()) {
                    val newImage = Image(imageURL = it, imageMealId = newMealId)
                    newImages.add(newImage)
                }
            }

            _mealFunctions.value?.functionMealId = newMealId

            mealRepository.saveMealWithRelations(
                meal = thisMeal,
                images = newImages,
                functions = _mealFunctions.value
            )
        }
    }


    //DELETE DATA LOGIC
    private var _onDeleteMealClick = MutableLiveData(false)
    val onDeleteMealClick: LiveData<Boolean>
        get() = _onDeleteMealClick

    fun onDeleteMealClick() {
        _onDeleteMealClick.value = true
    }

    fun onDelete() {
        val newImages = mutableListOf<Image>()
        imageURL.value?.let {
            if (it.isNotBlank()) {
                newImages.add(Image(imageURL = it))
            }
        }
        uiScope.launch {

            mealRepository.deleteMealWithRelations(
                meal = thisMeal,
                functions = mealFunctions.value,
                images = newImages)
        }
    }

    //MEAL FUNCTIONS LOGIC
    private var _mealFunctions = MutableLiveData(MealFunction(functionMealId = 0))

    val mealFunctions: LiveData<MealFunction>
        get() = _mealFunctions

    fun onStarchClick() {
        _mealFunctions.value?.let {
            it.starch = flipBoolean(it.starch)
        }
    }

    fun onVegClick() {
        _mealFunctions.value?.let {
            it.veg  = flipBoolean(it.veg)
        }
    }

    fun onBeverageClick() {
        _mealFunctions.value?.let {
            it.beverage  = flipBoolean(it.beverage)
        }
    }

    fun onProteinClick() {
        _mealFunctions.value?.let {
            it.protein  = flipBoolean(it.protein)
        }
    }

    fun onDessertClick() {
        _mealFunctions.value?.let {
            it.dessert  = flipBoolean(it.dessert)
        }
    }

    fun onIngredientClick() {
        _mealFunctions.value?.let {
            it.ingredient  = flipBoolean(it.ingredient)
        }
    }

    fun onDipClick() {
        _mealFunctions.value?.let {
            it.dip  = flipBoolean(it.dip)
        }
    }

    fun onDressingClick() {
        _mealFunctions.value?.let {
            it.dressing  = flipBoolean(it.dressing)
        }
    }

    //CANCEL JOB
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}



