package com.example.storyappsumbission.viewmodel

import androidx.lifecycle.ViewModel
import com.example.storyappsumbission.repository.UserRepository

class SignUpViewModel(private val repository: UserRepository): ViewModel() {
    fun signup(name:String, email:String, password:String)= repository.getRegister(name, email, password)


}