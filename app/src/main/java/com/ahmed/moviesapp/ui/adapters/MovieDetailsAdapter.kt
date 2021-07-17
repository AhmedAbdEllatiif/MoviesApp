package com.ahmed.moviesapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmed.moviesapp.data.MovieDetailsItem
import com.ahmed.moviesapp.databinding.MovieDetailItemBinding
import com.ahmed.moviesapp.di.AppModule
import javax.inject.Inject
import javax.inject.Named


class MovieDetailsAdapter: RecyclerView.Adapter<MovieDetailsAdapter.ViewHolder>() {


    lateinit var items:List<MovieDetailsItem>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val content = items[position].content
        val title = items[position].title
        holder.bind(title = title,content = content)
    }

    override fun getItemCount(): Int = items.size




    class ViewHolder(private val binding: MovieDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(title:String,content:String){
                binding.movieDetailTitle.text = title
                binding.movieDetailContent.text = content
            }

        }


}