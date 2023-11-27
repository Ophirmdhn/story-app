package com.ophi.storyapp.model

import androidx.lifecycle.ViewModel
import com.ophi.storyapp.repository.StoryRepository
import java.io.File

class UploadViewModel(private val repository: StoryRepository): ViewModel() {
    fun upload(file: File, description: String, lat: Double? = null, lon: Double? = null) =
        repository.upload(file, description, lat, lon)
}