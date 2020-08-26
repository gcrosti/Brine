package com.gdc.recipebook.screens.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomeViewModel(): ViewModel() {

    private var _onNewMealClick = MutableLiveData(false)
    val onNewMealClick: LiveData<Boolean>
        get() = _onNewMealClick

    fun onNewMealClick() {
        _onNewMealClick.value = true
    }

}

