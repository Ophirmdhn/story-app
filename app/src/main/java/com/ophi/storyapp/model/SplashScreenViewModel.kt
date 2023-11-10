package com.ophi.storyapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ophi.storyapp.pref.UserModel
import com.ophi.storyapp.repository.StoryRepository

class SplashScreenViewModel(private val repository: StoryRepository): ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}