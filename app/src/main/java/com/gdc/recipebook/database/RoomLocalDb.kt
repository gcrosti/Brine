package com.gdc.recipebook.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gdc.recipebook.database.dataclasses.*
import com.gdc.recipebook.database.interfaces.RoomDatabaseDAO


@Database(entities = [Image::class, Meal::class,MealFunction::class,Resource::class],version = 5,exportSchema = false)
abstract class RoomLocalDb: RoomDatabase() {
    abstract val databaseDAO: RoomDatabaseDAO

    companion object {
        @Volatile
        private var INSTANCE: com.gdc.recipebook.database.RoomLocalDb? = null

        fun getInstance(context: Context): com.gdc.recipebook.database.RoomLocalDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RoomLocalDb::class.java,
                        "meal_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}