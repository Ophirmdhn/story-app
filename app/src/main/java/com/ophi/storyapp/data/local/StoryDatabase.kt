package com.ophi.storyapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ophi.storyapp.pref.StoryModel


@Database(entities = [StoryModel::class], version = 1)

abstract class StoryDatabase: RoomDatabase() {

    abstract fun storyDao(): StoryDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, "quote_database"
                )
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}