package com.gdc.recipebook.screens.mealeditor.viewModel

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.*
import com.gdc.recipebook.database.Repository
import com.gdc.recipebook.database.RoomDatabaseDAO
import com.gdc.recipebook.database.dataclasses.*
import com.gdc.recipebook.screens.mealeditor.resources.ResourceListAdapter
import com.gdc.recipebook.screens.mealeditor.resources.ResourceListListener
import com.gdc.recipebook.screens.mealeditor.utils.flipBoolean
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

    //CREATE A NEW MEALID OR FIND EXISTING DATA FOR THIS MEAL
    private var mealId = 0L
    private var isNew = false
    private var mealWithRelations: MealWithRelations? = null

    fun setMealId() {
        uiScope.launch {
            val result = mealRepository.setMealId(mealName)
            Log.d("meal editor uiscope","running")
            mealId = result.first
            isNew = result.second
            if (!isNew) {
                mealWithRelations = mealRepository.retrieveMealWithRelations(mealName)
                Log.d("mealeditor retrieved", mealWithRelations.toString())
                mealNotes.value = mealWithRelations!!.meal.notes
                _mealFunctions.value = mealWithRelations!!.functions
                mealWithRelations!!.images?.let {
                    imageURL.value = it[it.lastIndex].imageURL
                }
            }
        }
    }

    //MEAL NAME LOGIC
    var mealName = ""

    val nameTextWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mealName = s.toString() } }

    //MEAL NOTES LOGIC
    var mealNotes = MutableLiveData("")

    val notesTextWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mealNotes.value = s.toString() } }


    //IMAGE LOGIC
    var imageURL = MutableLiveData("")
    private var onImageChanged = false

    val imageTextWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            imageURL.value = s.toString()
            Log.d("image being changed", imageURL.value!!)
            onImageChanged = true
        }
    }


    private var _onNewImageClick = MutableLiveData(false)
    val onNewImageClick: LiveData<Boolean>
        get() = _onNewImageClick

    fun onNewImageClick() {
        _onNewImageClick.value = true
    }

    fun addNewImageURL(url: String) {
        imageURL.value = url
    }



    //MEAL RESOURCES LOGIC
    private var _onAddResourcesClick = MutableLiveData(false)
    val onAddResourcesClick: LiveData<Boolean>
        get() = _onAddResourcesClick


    private var _resources = MutableLiveData(mutableListOf<Resource>())
    val resources
            get() = _resources

    fun onAddResourceClick() {
        _onAddResourcesClick.value = true
    }

    fun addNewResource(uri: String) {
        _resources.value!!.add(Resource(resourceURL = uri))
        Log.d("url added", resources.value.toString())
    }

    fun removeResource(resource: Resource) {
        Log.d("Resources",_resources.value.toString())
        _resources.value!!.remove(resource)
        Log.d("Resources post remove", _resources.value.toString())
    }


    //SAVE DATA LOGIC

    private var _onSaveMealClick = MutableLiveData(false)
    val onSaveMealClick: LiveData<Boolean>
        get() = _onSaveMealClick

    fun onSaveMealClick() {
        _onSaveMealClick.value = true
    }

    fun onSave() {
        uiScope.launch {

                val thisMeal = Meal(
                    mealId = mealId,
                    name = mealName,
                    notes = mealNotes.value!!
                )
                Log.d("meal to be saved",thisMeal.toString())

            var newImages: MutableList<Image>? = null

            imageURL.value?.let {
                if (onImageChanged) {
                    val newImage = Image(imageURL = it, imageMealId = mealId)
                    newImages = mutableListOf()
                    newImages!!.add(newImage)
                    Log.d("images to be saved",newImages.toString())
                }
            }

            _mealFunctions.value?.functionMealId = mealId

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
                meal = mealWithRelations?.meal,
                functions = mealWithRelations?.functions,
                images = mealWithRelations?.images)
        }
    }

    //MEAL FUNCTIONS LOGIC
    private var _mealFunctions = MutableLiveData(MealFunction(functionMealId = 0))

    val mealFunctions: MutableLiveData<MealFunction>
        get() = _mealFunctions

    fun onStarchClick() {
        _mealFunctions.value?.let {
            it.starch =
                flipBoolean(it.starch)
        }
    }

    fun onVegClick() {
        _mealFunctions.value?.let {
            it.veg  =
                flipBoolean(it.veg)
        }
    }

    fun onBeverageClick() {
        _mealFunctions.value?.let {
            it.beverage  =
                flipBoolean(it.beverage)
        }
    }

    fun onProteinClick() {
        _mealFunctions.value?.let {
            it.protein  =
                flipBoolean(it.protein)
        }
    }

    fun onDessertClick() {
        _mealFunctions.value?.let {
            it.dessert  =
                flipBoolean(it.dessert)
        }
    }

    fun onIngredientClick() {
        _mealFunctions.value?.let {
            it.ingredient  =
                flipBoolean(it.ingredient)
        }
    }

    fun onDipClick() {
        _mealFunctions.value?.let {
            it.dip  =
                flipBoolean(it.dip)
        }
    }

    fun onDressingClick() {
        _mealFunctions.value?.let {
            it.dressing  =
                flipBoolean(it.dressing)
        }
    }

    //CANCEL JOB
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}



