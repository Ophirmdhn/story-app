package com.ophi.storyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.ophi.storyapp.databinding.ActivityDetailBinding
import com.ophi.storyapp.model.DetailViewModel
import com.ophi.storyapp.model.ViewModelFactory
import com.ophi.storyapp.repository.Result

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showDetail()
    }

    private fun showDetail() {
        intent.getStringExtra(ID_STORY)?.let {
            viewModel.getDetail(it).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            Glide.with(binding.root.context)
                                .load(result.data.story.photoUrl)
                                .into(binding.ivDetailPhoto)
                            binding.tvDetailName.text = result.data.story.name
                            binding.tvDetailDescription.text = result.data.story.description
                        }

                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                            binding.tvDetailName.visibility = View.INVISIBLE
                            binding.tvDetailDescription.visibility = View.INVISIBLE
                            binding.tvError.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }

    companion object {
        var ID_STORY = "ID_STORY"
    }
}