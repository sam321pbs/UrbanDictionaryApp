package com.sammengistu.urbandictionaryapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sammengistu.urbandictionaryapp.models.Definition

@Database(entities = [Definition::class], version = 2)
abstract class UrbanDictionaryDb : RoomDatabase() {
    abstract fun definitionDao(): DefinitionDao

    companion object {
        private var instance: UrbanDictionaryDb? = null
        private const val DB_NAME = "dictionary"

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance
                    ?: Room.databaseBuilder(
                        context,
                        UrbanDictionaryDb::class.java, DB_NAME
                    ).build().also { instance = it }
            }
    }
}