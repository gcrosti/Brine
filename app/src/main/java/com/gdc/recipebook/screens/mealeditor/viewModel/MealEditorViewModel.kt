package com.gdc.recipebook.screens.mealeditor.viewModel

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdc.recipebook.database.ImagesFromEditor
import com.gdc.recipebook.database.Repository
import com.gdc.recipebook.database.ResourcesFromEditor
import com.gdc.recipebook.database.dataclasses.*
import com.gdc.recipebook.screens.mealeditor.resources.ResourceListAdapter
import com.gdc.recipebook.screens.mealeditor.resources.ResourceListListener
import com.gdc.recipebook.screens.mealeditor.utils.flipBoolean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MealEditorViewModel(private val repository: Repository): ViewModel() {

    //SET THREAD SCOPE
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private var mealId = 0L
    private var isNew = false
    private var mealWithRelations: MealWithRelations? = null
    private val imagesFromEditor = ImagesFromEditor()
    private val resourcesFromEditor = ResourcesFromEditor()


    fun getMealData() {
        uiScope.launch {
            val result = repository.getMealIdFromLocal(mealName)
            mealId = result.mealId
            isNew = result.isNew
            if (!isNew) {
                mealWithRelations = repository.getMealWithRelationsFromLocal(mealName)
                mealNotes.value = mealWithRelations!!.meal.notes

                mealWithRelations!!.functions?.let {
                    _mealFunctions.value = mealWithRelations!!.functions
                }

                mealWithRelations!!.images?.let {
                    _images.value = it

                    imagesFromEditor.loadedImages = it.toMutableList()
                    imagesFromEditor.savedImages = it.toMutableList()
                }

                mealWithRelations!!.resources?.let {
                    _resources.value = it

                    resourcesFromEditor.loadedResources = it.toMutableList()
                    resourcesFromEditor.savedResources = it.toMutableList()
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


    //MEAL IMAGES LOGIC
    private var _images = MutableLiveData<MutableList<Image>>(mutableListOf())
    val images: LiveData<MutableList<Image>>
        get() = _images

    private var _onNewImageClick = MutableLiveData(false)
    val onNewImageClick: LiveData<Boolean>
        get() = _onNewImageClick

    fun onNewImageClick() {
        _onNewImageClick.value = true
    }

    fun addNewImageURL(url: String) {
        if (url.isNotBlank()) {
            val images = _images.value
            images!!.add(Image(imageURL = url))
            _images.value = images
            imagesFromEditor.savedImages.add(Image(imageURL = url))
        }
    }

    fun removeImage(image: Image) {
        val images = _images.value
        images!!.remove(image)
        _images.value = images
        imagesFromEditor.savedImages.remove(image)
    }


    //MEAL RESOURCES LOGIC
    private var _onAddResourcesClick = MutableLiveData(false)
    val onAddResourcesClick: LiveData<Boolean>
        get() = _onAddResourcesClick

    val adapter = ResourceListAdapter(ResourceListListener { resource -> removeResource(resource) })

    private var _resources = MutableLiveData(mutableListOf<Resource>())
    val resources: LiveData<MutableList<Resource>>
            get() = _resources

    fun onAddResourceClick() {
        _onAddResourcesClick.value = true
    }

    fun addNewResource(uri: String) {
        _resources.value?.add(Resource(resourceURL = uri))

        resourcesFromEditor.savedResources.add(Resource(resourceURL = uri))
    }

    private fun removeResource(resource: Resource) {
        _resources.value?.remove(resource)
        adapter.notifyDataSetChanged()

        resourcesFromEditor.savedResources.remove(resource)
    }

    //SAVE DATA LOGIC

    private var _onSaveMealClick = MutableLiveData(false)
    val onSaveMealClick: LiveData<Boolean>
        get() = _onSaveMealClick

    fun onSaveMealClick() {
        _onSaveMealClick.value = true
    }

    fun onSave() {
        mealName = mealName.capitalize()
        uiScope.launch {

                val thisMeal = Meal(
                    mealId = mealId,
                    name = mealName,
                    notes = mealNotes.value!!
                )

            _mealFunctions.value?.functionMealId = mealId

            repository.saveMealWithRelations(
                meal = thisMeal,
                imagesFromEditor = imagesFromEditor,
                functions = mealFunctions.value,
                resourcesFromEditor = resourcesFromEditor
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
        uiScope.launch {

            repository.deleteMealWithRelations(
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



