package com.ahmed.moviesapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmed.moviesapp.data.MovieItem
import com.ahmed.moviesapp.databinding.MovieItemBinding

class MoviesAdapter :
    PagingDataAdapter<MovieItem, MoviesAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }


    class MovieViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // bind views
        fun bind(movieItem: MovieItem) {
            binding.apply {
                movieTitle.text = movieItem.title
                movieDate.text = movieItem.release_date
            }
        }
    }



    companion object {
        // DiffUtil
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean =
                oldItem.id == newItem.id

        }
    }
}