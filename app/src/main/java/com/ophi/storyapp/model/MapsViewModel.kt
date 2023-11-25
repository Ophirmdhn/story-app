package com.ophi.storyapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ophi.storyapp.data.response.ListStoryItem
import com.ophi.storyapp.repository.Result
import com.ophi.storyapp.repository.StoryRepository

class MapsViewModel(private val repository: StoryRepository): ViewModel() {
    fun getStoriesWithLocation(): LiveData<Result<List<ListStoryItem>>> {
        return repository.getStoriesWithLocation()
    }
}