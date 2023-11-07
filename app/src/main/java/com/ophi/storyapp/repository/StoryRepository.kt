package com.ophi.storyapp.repository

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.ophi.storyapp.data.response.LoginResponse
import com.ophi.storyapp.data.response.SignupResponse
import com.ophi.storyapp.data.retrofit.ApiService
import com.ophi.storyapp.pref.UserModel
import com.ophi.storyapp.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class StoryRepository private constructor(
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

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            isNeeded: Boolean
        ): StoryRepository? {
            if (isNeeded) {
                synchronized(this) {
                    instance = StoryRepository(apiService, userPreference)
                }
            }
            return instance
        }
    }
}