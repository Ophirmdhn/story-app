package com.ophi.storyapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ophi.storyapp.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStory.layoutManager = LinearLayoutManager(this)

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity, UploadActivity::class.java)
            startActivity(intent)
        }

        showStory()

//        viewModel.getSession().observe(this) { user ->
//
//        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.story()
    }

    private fun showStory() {
        viewModel.story().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        showLoading(false)
                        val adapter = StoryAdapter(result.data.listStory)
                        binding.rvStory.adapter = adapter
                    }

                    is Result.Error -> {
                        showLoading(false)
                        viewModel.getSession().observe(this) { user ->
                            Toast.makeText(this, user.token, Toast.LENGTH_LONG).show()
                        }

                        Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }
}