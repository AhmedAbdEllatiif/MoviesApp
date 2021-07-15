package com.ahmed.moviesapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmed.moviesapp.R
import com.ahmed.moviesapp.data.MovieItem
import com.ahmed.moviesapp.databinding.MovieItemBinding
import com.ahmed.moviesapp.databinding.ProgressbarWithTextBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class MoviesAdapter :
    PagingDataAdapter<MovieItem, MoviesAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {

    // LiveData of MovieItem
    val movieItemLiveData = MutableLiveData<MovieItem>()

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


    inner class MovieViewHolder(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // bind views
        fun bind(movieItem: MovieItem) {

            binding.apply {

                // set movie title & movie release date to the view
                movieTitle.text = movieItem.title
                movieDate.text = movieItem.release_date

                // set movie rate to the view
                handleRatingView(movieItem)

                // set movie poster
                handleMoviePoster(movieItem)

                // on itemView clicked
                handleOnItemClicked(movieItem)
            }
        }

        private fun handleRatingView(movieItem: MovieItem) {
            val progressbarBinding: ProgressbarWithTextBinding = binding.progressBarContainer
            binding.apply {
                val rounded = String.format("%.0f", movieItem.user_rating * 10f)
                val ratePercentage = "${rounded}%"
                progressbarBinding.progressText.text = ratePercentage
                progressbarBinding.progressBar.progress = (movieItem.user_rating * 10f).toInt()
                progressbarBinding.progressBar.max = 100
            }
        }

        private fun handleMoviePoster(movieItem: MovieItem) {
            binding.apply {
                Glide.with(itemView)
                    .load("http://image.tmdb.org/t/p/w300${movieItem.movie_poster}")
                    .placeholder(R.drawable.rounded_bg)
                    .thumbnail(0.1f)
                    .transform(CenterCrop(), RoundedCorners(15))
                    .into(movieImage)
            }
        }

        private fun handleOnItemClicked(movieItem: MovieItem) {
            binding.movieCardItem.setOnClickListener {
                movieItemLiveData.value = movieItem
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