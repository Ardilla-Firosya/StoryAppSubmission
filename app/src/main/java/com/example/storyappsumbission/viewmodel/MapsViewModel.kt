package com.example.storyappsumbission.viewmodel

import androidx.lifecycle.ViewModel
import com.example.storyappsumbission.repository.UserRepository

class MapsViewModel(private val repository: UserRepository) : ViewModel() {
    fun getLocationStory() = repository.getLocation()

}