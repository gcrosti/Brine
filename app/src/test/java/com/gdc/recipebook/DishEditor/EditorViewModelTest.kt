package com.gdc.recipebook.DishEditor

import com.gdc.recipebook.data.FakeRepository
import org.junit.Rule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.gdc.recipebook.database.ImagesFromEditor
import com.gdc.recipebook.database.RepositoryInterface
import com.gdc.recipebook.database.dataclasses.Image
import com.gdc.recipebook.database.dataclasses.Meal
import com.gdc.recipebook.database.dataclasses.MealFunction
import com.gdc.recipebook.database.dataclasses.MealWithRelations
import com.gdc.recipebook.screens.mealeditor.viewModel.MealEditorViewModel
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test


class EditorViewModelTest {


    private lateinit var dishesRepository: RepositoryInterface

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var editorViewModel: MealEditorViewModel

    @Before
    fun setupViewModel() {
        dishesRepository = FakeRepository()
        editorViewModel = MealEditorViewModel(dishesRepository)

    }


    @Test
    fun getDishData_loadsExistingDish() {
        //Given dish data saved in the Repository
        val dish = Meal(mealId = 1, name = "test1Name",notes = "test1Notes")
        val functions = MealFunction(functionMealId = dish.mealId)
        functions.starch = true
        val images = mutableListOf(
            Image(imageId = 0L, imageMealId = dish.mealId), Image(imageId = 1L, imageMealId = dish.mealId))

        val mealWithRelations = MealWithRelations(meal = dish, images = images, functions = functions)
        runBlocking {
            dishesRepository.saveMealWithRelations(
                meal = dish,
                imagesFromEditor = ImagesFromEditor(savedImages = images),
                functions = functions)
        }

        //When the dish is loaded by the ViewModel
        editorViewModel.getDishData()
        editorViewModel.mealName = dish.name

        //Then all of the correct data is loaded
        assertThat(editorViewModel.mealName, `is`(dish.name))
        //assertThat(editorViewModel.mealNotes.value, `is`(dish.notes))
        assertThat(editorViewModel.images.value, `is`(mealWithRelations.images))
    }
}