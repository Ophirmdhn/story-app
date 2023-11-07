package com.ophi.storyapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ophi.storyapp.pref.UserModel
import com.ophi.storyapp.repository.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: StoryRepository): ViewModel() {
    fun login(email: String, password: String) = repository.login(email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}