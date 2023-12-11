package com.example.storyappsumbission.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyappsumbission.R
import com.example.storyappsumbission.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    companion object{
        val EXTRA_PHOTO = "extraPhoto"
        val EXTRA_NAME = "extraName"
        val EXTRA_DESCRIPTION = "extraDescription"
    }
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val name= intent.getStringExtra(EXTRA_NAME)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val imgPhoto = intent.getStringExtra(EXTRA_PHOTO)

        supportActionBar?.title = "Detail Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tvName.text = name
        Glide.with(this).load(imgPhoto)
            .into(binding.imgStory)
        binding.tvDescription.text = description

    }
}