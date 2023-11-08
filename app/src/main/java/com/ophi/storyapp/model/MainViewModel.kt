package com.ophi.storyapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ophi.storyapp.pref.UserModel
import com.ophi.storyapp.repository.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository) : ViewModel() {
    fun story() = repository.stories()

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch { repository.logout() }
    }
}