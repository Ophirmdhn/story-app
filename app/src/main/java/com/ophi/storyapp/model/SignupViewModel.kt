package com.ophi.storyapp.model

import androidx.lifecycle.ViewModel
import com.ophi.storyapp.repository.StoryRepository

class SignupViewModel(private val repository: StoryRepository): ViewModel() {
    fun signup(name: String, email: String, password: String) =
        repository.signup(name, email, password)
}