package com.gdc.recipebook.screens.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdc.recipebook.database.Repository
import com.gdc.recipebook.database.RoomDatabaseDAO
import com.gdc.recipebook.database.dataclasses.MealWithFunctions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WelcomeViewModel(): ViewModel() {

    private var _onNewMealClick = MutableLiveData(false)
    val onNewMealClick: LiveData<Boolean>
        get() = _onNewMealClick

    fun onNewMealClick() {
        _onNewMealClick.value = true
    }

}

