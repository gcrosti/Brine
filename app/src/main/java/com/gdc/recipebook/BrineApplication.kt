package com.gdc.recipebook

import android.app.Application
import com.gdc.recipebook.database.Repository

class BrineApplication: Application() {
    val repository: Repository
        get() = Repository.getRepository(this)
}