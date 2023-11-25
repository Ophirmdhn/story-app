package com.ophi.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ophi.storyapp.data.response.ListStoryItem
import com.ophi.storyapp.databinding.StoryItemBinding
import com.ophi.storyapp.ui.DetailActivity

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    inner class MyViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(storyList: ListStoryItem) {
                Glide.with(binding.root)
                    .load(storyList.photoUrl)
                    .into(binding.ivItemPhoto)
                binding.tvItemName.text = storyList.name

                binding.root.setOnClickListener {
                    val intentDetail = Intent(binding.root.context, DetailActivity::class.java)
                    intentDetail.putExtra(DetailActivity.ID, storyList.id)
                    intentDetail.putExtra(DetailActivity.NAME, storyList.name)
                    intentDetail.putExtra(DetailActivity.DESCRIPTION, storyList.description)
                    intentDetail.putExtra(DetailActivity.PICTURE, storyList.photoUrl)
                    binding.root.context.startActivity(intentDetail)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}