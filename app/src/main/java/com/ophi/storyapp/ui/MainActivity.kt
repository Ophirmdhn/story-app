package com.ophi.storyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ophi.storyapp.R
import com.ophi.storyapp.adapter.LoadingStateAdapter
import com.ophi.storyapp.adapter.StoryAdapter
import com.ophi.storyapp.databinding.ActivityMainBinding
import com.ophi.storyapp.model.MainViewModel
import com.ophi.storyapp.model.ViewModelFactory
import com.ophi.storyapp.repository.Result

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    private var storyAdapter = StoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStory.layoutManager = LinearLayoutManager(this)

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity, UploadActivity::class.java)
            startActivity(intent)
        }

        viewModel.getSession().observe(this) { user ->
            binding.topAppBar.title = getString(R.string.main_title, user.name)
        }


        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        viewModel.story.observe(this) { story ->
            storyAdapter.submitData(lifecycle, story)
        }
    }

    fun maps(item: MenuItem) {
        val intent = Intent(this@MainActivity, MapsActivity::class.java)
        startActivity(intent)
    }

    fun language(item: MenuItem) {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    fun logout(item: MenuItem) {
        viewModel.logout()
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}