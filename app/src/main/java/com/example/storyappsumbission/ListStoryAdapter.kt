package com.example.storyappsumbission

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyappsumbission.databinding.ItemRowStoryBinding
import com.example.storyappsumbission.response.ListStoryItem
import com.example.storyappsumbission.ui.DetailActivity


class ListStoryAdapter : PagingDataAdapter<ListStoryItem, ListStoryAdapter.StoryViewHolder>(DIFF_CALLBACK){

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
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

    inner class StoryViewHolder(private val binding: ItemRowStoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind (user: ListStoryItem){
            binding.tvName.text = user.name
            binding.tvDescription.text = user.description
            Glide.with(itemView.context).load(user.photoUrl)
                .into(binding.imageStory)

            binding.itemRowStory.setOnClickListener{
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_NAME,user.name)
                intent.putExtra(DetailActivity.EXTRA_DESCRIPTION,user.description)
                intent.putExtra(DetailActivity.EXTRA_PHOTO,user.photoUrl)
                itemView.context.startActivity(intent)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListStoryAdapter.StoryViewHolder {
        return StoryViewHolder(ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: ListStoryAdapter.StoryViewHolder, position: Int) {
        val userData = getItem(position)
        holder.bind(userData!!)
    }

}

