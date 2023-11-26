package com.ophi.storyapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.ophi.storyapp.data.database.StoryDatabase
import com.ophi.storyapp.data.response.ErrorResponse
import com.ophi.storyapp.data.response.ListStoryItem
import com.ophi.storyapp.data.response.LoginResponse
import com.ophi.storyapp.data.response.SignupResponse
import com.ophi.storyapp.data.response.StoryResponse
import com.ophi.storyapp.data.response.UploadResponse
import com.ophi.storyapp.data.retrofit.ApiService
import com.ophi.storyapp.pref.UserModel
import com.ophi.storyapp.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    fun signup(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.signup(name, email, password)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(jsonInString, SignupResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.login(email, password)
            val userModel = UserModel(
                successResponse.loginResult.name, successResponse.loginResult.token, true)
            saveSession(userModel)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(jsonInString, LoginResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    fun upload(imageFile: File, description: String, lat: Double?, lon: Double?) = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData("photo",imageFile.name,requestImageFile)
        val requestLat = lat?.toString()?.toRequestBody()
        val requestLon = lon?.toString()?.toRequestBody()

        try {
            val successResponse = apiService.uploadStories(multipartBody, requestBody, requestLat, requestLon)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(jsonInString, UploadResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(database, apiService),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getStoriesWithLocation(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation()
            emit(Result.Success(response.listStory))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            database: StoryDatabase,
            apiService: ApiService,
            userPreference: UserPreference,
            isNeeded: Boolean
        ): StoryRepository? {
            if (isNeeded) {
                synchronized(this) {
                    instance = StoryRepository(database, apiService, userPreference)
                }
            }
            return instance
        }
    }
}