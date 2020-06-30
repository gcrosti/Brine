package com.gdc.recipebook.screens.mealeditor.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class EditorLifecycleObserver(
    private val lifecycle: Lifecycle, private val actionSave: () -> Unit): LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun activityResumed() {
        actionSave.invoke()
    }

}