package com.ophi.storyapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ophi.storyapp.pref.StoryModel

@Dao
interface StoryDao {

    @Insert
    suspend fun insertStory(story: List<StoryModel>)

    @Query("SELECT * FROM story")
    fun getAllStory(): LiveData<List<StoryModel>>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}