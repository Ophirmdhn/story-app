package com.ophi.storyapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ophi.storyapp.data.response.ListStoryItem
import com.ophi.storyapp.databinding.StoryItemBinding
import com.ophi.storyapp.ui.DetailActivity

class StoryAdapter(private val listStory: List<ListStoryItem>) :
    RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {

    class ListViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(storyList: ListStoryItem) {
                Glide.with(binding.root.context)
                    .load(storyList.photoUrl)
                    .into(binding.ivItemPhoto)
                binding.tvItemName.text = storyList.name

                itemView.setOnClickListener {
                    val storyId = storyList.id
                    val intentDetail = Intent(binding.root.context, DetailActivity::class.java)
                    intentDetail.putExtra(DetailActivity.ID_STORY, storyId)
                    binding.root.context.startActivity(intentDetail)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = listStory[position]
        holder.bind(story)
    }
}