package com.gdc.recipebook.screens.mealeditor

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts


class PhotoActivityResultContract: ActivityResultContracts.GetContent() {
    override fun createIntent(context: Context, input: String): Intent {
        val intent = super.createIntent(context,input)
        intent.action = Intent.ACTION_OPEN_DOCUMENT

        return intent
    }
}