package com.gdc.recipebook.screens.mealeditor.viewModel

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdc.recipebook.database.interfaces.ImagesFromEditor
import com.gdc.recipebook.database.interfaces.RepositoryInterface
import com.gdc.recipebook.database.interfaces.ResourcesFromEditor
import com.gdc.recipebook.database.dataclasses.*
import com.gdc.recipebook.screens.mealeditor.resources.ResourceListAdapter
import com.gdc.recipebook.screens.mealeditor.utils.flipBoolean
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MealEditorViewModel(private val repository: RepositoryInterface): ViewModel() {

    //SET THREAD SCOPE
    private var viewModelJob = Job()


    private var mealId = 0L
    private var isNew = false
    private var mealWithRelations: MealWithRelations? = null
    private val imagesFromEditor =
        ImagesFromEditor()
    private val resourcesFromEditor =
        ResourcesFromEditor()


    fun getDishData() {

        viewModelScope.launch {
            val result = repository.getRoomMealId(mealName)
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
        }
    }

    fun removeImage(image: Image) {
        val images = _images.value
        images!!.remove(image)
        _images.value = images
    }


    //MEAL RESOURCES LOGIC
    private var _onAddResourcesClick = MutableLiveData(false)
    val onAddResourcesClick: LiveData<Boolean>
        get() = _onAddResourcesClick

    private lateinit var adapter: ResourceListAdapter

    fun setAdapter(ad: ResourceListAdapter) {
        adapter = ad
    }

    private var _resources = MutableLiveData(mutableListOf<Resource>())
    val resources: LiveData<MutableList<Resource>>
            get() = _resources

    fun onAddResourceClick() {
        _onAddResourcesClick.value = true
    }

    fun addNewResource(uri: String) {
        _resources.value?.add(Resource(resourceURL = uri))
    }

    fun removeResource(resource: Resource) {
        _resources.value?.remove(resource)
        adapter.notifyDataSetChanged()
    }

    //SAVE DATA LOGIC

    private var _onSaveMealClick = MutableLiveData(false)
    val onSaveMealClick: LiveData<Boolean>
        get() = _onSaveMealClick

    fun onSaveMealClick() {
        _onSaveMealClick.value = true
    }

    fun saveDishData() {

        val dataForRepo = prepareDataForRepo()

        viewModelScope.launch {
            repository.saveMealWithRelations(
                meal = dataForRepo.dish,
                imagesFromEditor = dataForRepo.images,
                functions = dataForRepo.functions,
                resourcesFromEditor = dataForRepo.resources
            )
        }
    }

    fun prepareDataForRepo(): DataForRepo {
        mealName = mealName.capitalize()
        imagesFromEditor.savedImages = images.value!!
        resourcesFromEditor.savedResources = resources.value!!
        _mealFunctions.value?.functionMealId = mealId
        val thisMeal = Meal(
            mealId = mealId,
            name = mealName,
            notes = mealNotes.value!!
        )
        return DataForRepo(thisMeal,imagesFromEditor,mealFunctions.value,resourcesFromEditor)
    }




    //DELETE DATA LOGIC
    private var _onDeleteMealClick = MutableLiveData(false)
    val onDeleteMealClick: LiveData<Boolean>
        get() = _onDeleteMealClick

    fun onDeleteMealClick() {
        _onDeleteMealClick.value = true
    }

    fun onDelete() {
        viewModelScope.launch {

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