package com.example.storyappsumbission.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappsumbission.data.UserModel
import com.example.storyappsumbission.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel (private val repository: UserRepository): ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
    fun login(email:String, password: String)= repository.getLogin(email, password)
}