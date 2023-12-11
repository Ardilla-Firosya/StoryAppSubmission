package com.example.storyappsumbission.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappsumbission.ListStoryAdapter
import com.example.storyappsumbission.Paging.LoadingStateAdapter
import com.example.storyappsumbission.R
import com.example.storyappsumbission.databinding.ActivityMainBinding
import com.example.storyappsumbission.viewmodel.MainViewModel
import com.example.storyappsumbission.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var  adapter: ListStoryAdapter

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.Upload.setOnClickListener{
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        viewModel.getSession().observe(this){ user ->
            if (!user.isLogin){
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
           } else{
                val adapter = ListStoryAdapter()
                binding.rvStory.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter{
                        adapter.retry()
                    }
                )
                viewModel.getStoryUser().observe(this){user ->
                    adapter.submitData(lifecycle, user)

               }
            }
        }
        supportActionBar?.show()
        setupView()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_logout -> {

                viewModel.logout()
                var intentSetting = Intent(this@MainActivity, WelcomeActivity::class.java)
                startActivity(intentSetting)
            }
            R.id.action_map-> {
                var intentMap = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intentMap)
            }

        }
        return super.onOptionsItemSelected(item)
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

}