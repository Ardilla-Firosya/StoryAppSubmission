package com.example.storyappsumbission.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyappsumbission.Paging.StoryPagingSource
import com.example.storyappsumbission.data.UiState
import com.example.storyappsumbission.data.UserModel
import com.example.storyappsumbission.data.UserPreference
import com.example.storyappsumbission.network.ApiConfig
import com.example.storyappsumbission.network.ApiService
import com.example.storyappsumbission.response.AddResponse
import com.example.storyappsumbission.response.ErrorResponse
import com.example.storyappsumbission.response.ListStoryItem
import com.example.storyappsumbission.response.LoginResponse
import com.example.storyappsumbission.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.net.SocketTimeoutException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout(){
        userPreference.logout()
    }
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference, apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }

    fun getLocation():LiveData<UiState<List<ListStoryItem>>> = liveData {
        emit(UiState.Loading)
        try {
            val userPref = runBlocking { userPreference.getSession().first() }
            val response = ApiConfig.getApiService(userPref.token)
            val locationResponse = response.getStoriesWithLocation()
            val location = locationResponse.listStory

            if (locationResponse.error == false){
                emit(UiState.Success(location.filterNotNull()))
            }else{
                emit(UiState.Error(locationResponse.message ?: "Error"))
            }

        }catch (e:HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody?.message ?: "An error occurred"
            emit(UiState.Error("Failed: $errorMessage"))
        }
    }

    fun getRegister(
        name: String,
        email: String,
        password: String
    ): LiveData<UiState<RegisterResponse>> = liveData {
        emit(UiState.Loading)
        try {
            val response = apiService.register(name, email, password)
            if (response.error == false) {
                emit(UiState.Success(response))
            } else {
                emit(UiState.Error(response.message ?: "An error occurred"))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody?.message ?: "An error occurred"
            emit(UiState.Error("Login error: $errorMessage"))
        }
    }
    fun getLogin(
        email: String,
        password: String
    ): LiveData<UiState<LoginResponse>> = liveData {
        emit(UiState.Loading)
        try {
            val response = apiService.login(email, password)
            if (response.error == false) {
                val tokenAuth = UserModel(name= response.loginResult.name?: "", userId = response.loginResult.userId?: " ", token = response.loginResult.token?: "", isLogin = true)
                ApiConfig.token= response.loginResult.token
                userPreference.saveSession(tokenAuth)
                emit(UiState.Success(response))
            } else {
                emit(UiState.Error(response.message ?: "An error occurred"))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody?.message ?: "An error occurred"
            emit(UiState.Error("Login error: $errorMessage"))
        }
    }
    fun postStory(photoFile: File, description: String) = liveData {
        emit(UiState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestPhotoProfile = photoFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData("photo", photoFile.name,requestPhotoProfile)
        try {
            val userPref = runBlocking {
                userPreference.getSession().first()
            }
            val response = ApiConfig.getApiService(userPref.token)
            val successResponse = response.uploadImage(multipartBody,requestBody)
            emit(UiState.Success(successResponse))

        }catch (ex:HttpException){
            val errorBody = ex.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody,AddResponse::class.java)
            emit(errorResponse?.message?.let { UiState.Error(it) })
        }
    }




}
