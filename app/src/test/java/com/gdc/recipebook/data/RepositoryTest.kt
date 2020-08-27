package com.gdc.recipebook.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gdc.recipebook.MainCoroutineRule
import com.gdc.recipebook.database.Repository
import com.gdc.recipebook.database.dataclasses.*
import com.gdc.recipebook.database.interfaces.ImagesFromEditor
import com.gdc.recipebook.database.interfaces.ResourcesFromEditor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScope
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class RepositoryTest {

    //Data
    private val mealWithRelations = MealWithRelations(
        meal = Meal(mealId = 1L, name = "AUnitTest",notes = "Test notes"),
        functions = MealFunction(functionId = 1L, functionMealId = 1L),
        resources = mutableListOf(Resource(resourceId = 1L,resourceMealId = 1L, resourceURL = "www.ATestResource.org")),
        images = mutableListOf(Image(imageMealId = 1L, imageId = 1L, imageURL = "www.ATestImage.org"))
    )

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    //Class Under Test
    lateinit var repository: Repository

    @Before
    fun buildRepository() {
        val fakeFirebaseDataManager = FakeFirebaseDataManager(mealWithRelations)
        val fakeRoomDataManager = FakeRoomDataManager(mealWithRelations)
        repository = Repository(
            roomDataManager = fakeRoomDataManager,
            firebaseDataManager = fakeFirebaseDataManager,
            uiScope = TestCoroutineScope(mainCoroutineRule.coroutineContext)
        )
    }


    @Test
    fun updateMealsWithFunctions_UpdatesMealsWithFunctions() {
        repository.updateMealsWithFunctions()
        val mealsWithFunctions = listOf(MealWithFunctions(mealWithRelations.meal,mealWithRelations.functions))

        assertThat(repository.mealsWithFunctions.value, `is` (mealsWithFunctions))
    }

    @Test
    fun saveMealData_MealDataLoadsProperly() {
        val ID_SAVED = 2L
        val savedMealWithRelations = MealWithRelations(
            meal = Meal(mealId = ID_SAVED,name = "Saved Meal", notes = "Saved Notes"),
            resources = mutableListOf(Resource(resourceId = ID_SAVED,resourceMealId = ID_SAVED,resourceURL = "url_saved")),
            images = mutableListOf(Image(imageId = ID_SAVED, imageMealId = ID_SAVED, imageURL = "url_saved")),
            functions = MealFunction(functionMealId = ID_SAVED,functionId = ID_SAVED))

        runBlocking {  repository.saveMealWithRelations(
            meal = savedMealWithRelations.meal,
            imagesFromEditor = ImagesFromEditor(listOf(),mealWithRelations.images!!),
            functions = mealWithRelations.functions,
            resourcesFromEditor = ResourcesFromEditor(listOf(),savedMealWithRelations.resources!!)
        ) }

        val loadedMealWithRelations = runBlocking { repository.getMealWithRelationsFromLocal(savedMealWithRelations.meal.name) }


        assertThat(savedMealWithRelations, `is`(loadedMealWithRelations))
    }

}