package com.gdc.recipebook.DishEditor

import com.gdc.recipebook.data.FakeRepository
import org.junit.Rule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.gdc.recipebook.MainCoroutineRule
import com.gdc.recipebook.database.ImagesFromEditor
import com.gdc.recipebook.database.RepositoryInterface
import com.gdc.recipebook.database.dataclasses.*
import com.gdc.recipebook.screens.mealeditor.viewModel.MealEditorViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test


class EditorViewModelTest {


    private lateinit var dishesRepository: RepositoryInterface
    private lateinit var editorViewModel: MealEditorViewModel
    private lateinit var dish: Meal
    private lateinit var functions: MealFunction
    private lateinit var images: MutableList<Image>


    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        dishesRepository = FakeRepository()
        editorViewModel = MealEditorViewModel(dishesRepository)
        dish = Meal(mealId = 1, name = "test1Name",notes = "test1Notes")
        functions = MealFunction(functionMealId = dish.mealId)
        images = mutableListOf(
            Image(imageId = 0L, imageMealId = dish.mealId), Image(imageId = 1L, imageMealId = dish.mealId))

    }

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun getDishData_loadsExistingDish() {
        //Given dish data saved in the Repository
        functions.starch = true
        runBlocking {
            dishesRepository.saveMealWithRelations(
                meal = dish,
                imagesFromEditor = ImagesFromEditor(savedImages = images),
                functions = functions)
        }

        //When the dish is loaded by the ViewModel
        editorViewModel.getDishData()



        //Then all of the correct data is loaded
        assertThat(editorViewModel.mealNotes.value, `is`(dish.notes))
        assertThat(editorViewModel.images.value, `is`(images))
    }

    @Test
    fun editedData_preparedCorrectlyForRepo() {
        //Given edited dish data
        runBlocking {
            dishesRepository.saveMealWithRelations(
                meal = dish,
                imagesFromEditor = ImagesFromEditor(savedImages = images),
                functions = functions)
        }
        val testNotes = "new test note"
        val testName = "New test name"
        editorViewModel.getDishData()
        editorViewModel.removeImage(images[0])
        editorViewModel.mealNotes.value = testNotes
        editorViewModel.mealName = testName
        editorViewModel.onDessertClick()
        editorViewModel.onBeverageClick()
        editorViewModel.onDipClick()
        editorViewModel.onDressingClick()
        editorViewModel.onProteinClick()
        editorViewModel.onStarchClick()
        editorViewModel.onIngredientClick()
        editorViewModel.onVegClick()

        //When data is prepared for Repo
        val preparedData = editorViewModel.prepareDataForRepo()

        //Then all data is prepared correctly
        assertThat(preparedData.images.loadedImages, `is`(images as List<Image>))
        assertThat((preparedData.images.savedImages), `is`(editorViewModel.images.value))
        assertThat(preparedData.dish, `is`(Meal(dish.mealId,testName,testNotes)))
        assertThat(preparedData.functions, `is`(editorViewModel.mealFunctions.value))
        assertThat(preparedData.resources.savedResources, `is` (editorViewModel.resources.value))

    }
}