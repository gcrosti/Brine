package com.gdc.recipebook.screens.mealeditor.viewModel

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdc.recipebook.database.Repository
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

    //CREATE A NEW MEALID OR FIND EXISTING DATA FOR THIS MEAL
    private var mealId = 0L
    private var isNew = false
    private var mealWithRelations: MealWithRelations? = null


    fun setMealId() {
        uiScope.launch {
            val result = Repository.setMealId(mealName)
            mealId = result.first
            isNew = result.second
            if (!isNew) {
                mealWithRelations = Repository.retrieveMealWithRelations(mealName)
                mealNotes.value = mealWithRelations!!.meal.notes

                mealWithRelations!!.functions?.let {
                    _mealFunctions.value = mealWithRelations!!.functions
                }

                mealWithRelations!!.images?.let {
                    _images.value = it

                    //Remember images originally loaded to correctly remove if necessary
                    setLoadedImages(it)
                }

                mealWithRelations!!.resources?.let {
                    _resources.value = it

                    // Remember resources originally loaded to correctly remove if necessary
                    setLoadedResources(it)
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


    //IMAGES LOGIC
    private var _images = MutableLiveData<MutableList<Image>>(mutableListOf())
    val images: LiveData<MutableList<Image>>
        get() = _images

    private var onImagesChanged = false

    private var _onNewImageClick = MutableLiveData(false)
    val onNewImageClick: LiveData<Boolean>
        get() = _onNewImageClick

    fun onNewImageClick() {
        _onNewImageClick.value = true
    }

    fun addNewImageURL(url: String) {
        val images = _images.value
        images!!.add(Image(imageURL = url))
        _images.value = images
        onImagesChanged = true
    }

    fun removeImage(image: Image) {
        val images = _images.value
        images!!.remove(image)
        _images.value = images
        onImagesChanged = true
        Log.d("images after remove",_images.value.toString())
    }

    private var loadedImages: MutableList<String>? = null
    private fun setLoadedImages(images: List<Image>) {
        loadedImages = mutableListOf()
        for (image in images) {
            loadedImages!!.add(image.imageURL)
        }
    }




    //MEAL RESOURCES LOGIC
    private var _onAddResourcesClick = MutableLiveData(false)
    val onAddResourcesClick: LiveData<Boolean>
        get() = _onAddResourcesClick

    private var onResourcesChanged = false


    val adapter = ResourceListAdapter(ResourceListListener { resource -> removeResource(resource) })

    private var _resources = MutableLiveData(mutableListOf<Resource>())
    val resources: LiveData<MutableList<Resource>>
            get() = _resources

    fun onAddResourceClick() {
        _onAddResourcesClick.value = true
    }

    fun addNewResource(uri: String) {
        _resources.value?.add(Resource(resourceURL = uri))
        onResourcesChanged = true
    }

    private fun removeResource(resource: Resource) {
        _resources.value?.remove(resource)
        adapter.notifyDataSetChanged()
        onResourcesChanged = true
    }


    private var loadedResources: List<String>? = null
    private fun setLoadedResources(resources: List<Resource>) {
        val incomingUrls = mutableListOf<String>()
        for (resource in resources) {
            incomingUrls.add(resource.resourceURL)
        }
        loadedResources = incomingUrls
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

            if(onImagesChanged) {
                for(image in _images.value!!) {
                    image.imageMealId = mealId
                }
            } else {_images.value = null}


            if (onResourcesChanged) {
                for (resource in _resources.value!!) {
                    resource.resourceMealId = mealId
                }
            } else {_resources.value = null }

            _mealFunctions.value?.functionMealId = mealId

            Repository.saveMealWithRelations(
                meal = thisMeal,
                loadedImages = loadedImages,
                savedImages = images.value,
                functions = mealFunctions.value,
                loadedResources = loadedResources,
                savedResources = resources.value
            )

            onImagesChanged = false
            onResourcesChanged = false
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

            Repository.deleteMealWithRelations(
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



