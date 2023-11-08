package com.ophi.storyapp.model

import androidx.lifecycle.ViewModel
import com.ophi.storyapp.repository.StoryRepository

class DetailViewModel(private val repository: StoryRepository): ViewModel() {
    fun getDetail(id: String) = repository.detail(id)
}