package com.ophi.storyapp.di

import android.content.Context
import com.ophi.storyapp.data.retrofit.ApiConfig
import com.ophi.storyapp.pref.UserPreference
import com.ophi.storyapp.pref.dataStore
import com.ophi.storyapp.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository? {
        val pref = UserPreference.getInstance(context.dataStore)

        val token = runBlocking {
            pref.getSession().first().token
        }

        val apiService = ApiConfig.getApiService(token)
        return StoryRepository.getInstance(apiService, pref, true)
    }
}