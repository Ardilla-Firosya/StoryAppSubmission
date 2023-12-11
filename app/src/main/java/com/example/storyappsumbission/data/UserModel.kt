package com.example.storyappsumbission.data

data class UserModel(
    val name: String,
    val userId: String,
    val token: String,
    val isLogin: Boolean = false
)
