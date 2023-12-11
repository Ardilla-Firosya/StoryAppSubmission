package com.example.storyappsumbission.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyappsumbission.data.UserModel
import com.example.storyappsumbission.repository.UserRepository
import com.example.storyappsumbission.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository): ViewModel() {

    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }
    fun getSession(): LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }

    fun getStoryUser():LiveData<PagingData<ListStoryItem>> = repository.getStory().cachedIn(viewModelScope)
}