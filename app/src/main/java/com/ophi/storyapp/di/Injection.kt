package com.ophi.storyapp.di

import android.content.Context
import com.ophi.storyapp.data.database.StoryDatabase
import com.ophi.storyapp.data.retrofit.ApiConfig
import com.ophi.storyapp.pref.UserPreference
import com.ophi.storyapp.pref.dataStore
import com.ophi.storyapp.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository? {
        val database = StoryDatabase.getDatabase(context)

        val pref = UserPreference.getInstance(context.dataStore)

        val user = runBlocking { pref.getSession().first() }

        val apiService = ApiConfig.getApiService(user.token)

        return StoryRepository.getInstance(database, apiService, pref, true)
    }
}