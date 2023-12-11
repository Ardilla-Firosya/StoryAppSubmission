package com.example.storyappsumbission.viewmodel

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.storyappsumbission.repository.UserRepository
import java.io.File

class StoryViewModel(private val userRepository: UserRepository) : ViewModel(){
    fun postImage(photo: File, description: String) = userRepository.postStory(photo, description)

}