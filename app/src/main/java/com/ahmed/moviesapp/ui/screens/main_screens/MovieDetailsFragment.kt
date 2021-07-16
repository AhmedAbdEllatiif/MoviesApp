package com.ahmed.moviesapp.ui.screens.main_screens


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmed.moviesapp.R
import com.ahmed.moviesapp.databinding.FragmentDetailsMovieBinding
import com.ahmed.moviesapp.databinding.ProgressbarWithTextBinding
import com.ahmed.moviesapp.di.ApiModule
import com.ahmed.moviesapp.ui.adapters.MovieDetailsAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_details_movie) {

    @Inject
    @Named(ApiModule.BASE_IMAGE_URL_W500)
    lateinit var baseImageUrl: String

    @Inject
    @Named(ApiModule.BASE_IMAGE_URL_W300)
    lateinit var smallBaseImageUrl: String

    @Inject
    lateinit var adapter: MovieDetailsAdapter

    // ViewModel
    private val viewModel: MainViewModel by activityViewModels()

    // mainBinding
    private var _binding: FragmentDetailsMovieBinding? = null
    private val binding get() = _binding!!

    // ProgressBar binding
    private val progressBarBinding: ProgressbarWithTextBinding = binding.progressBarContainer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialize _binding
        _binding = FragmentDetailsMovieBinding.bind(view)

        // observe movie details
        observeMovieDetails()
    }


    private fun observeMovieDetails(){
        viewModel.movieItemLiveData.observe(viewLifecycleOwner, { movieItem ->
            val movieImage = movieItem.movie_poster
            val originalTitle = movieItem.original_title
            val overview = movieItem.plot_synopsis
            val userRating = movieItem.user_rating
            val releaseDate = movieItem.release_date

            // bind the movie title
            binding.collapsingToolbar.title = originalTitle

            // bind movie image
            bindMovieImage(movieImageUrl = movieImage)

            // bind user rating
            bindUserRating(userRating = userRating)

            // bind movie details
            bindRvWithMovieDetails(
                overview = overview,
                userRating = userRating.toString(),
                releaseDate = releaseDate
            )

        })
    }


    /**
     * To bind the movie image
     * */
    private fun bindMovieImage(movieImageUrl: String) {
        Glide.with(requireActivity())
            .load("$baseImageUrl$movieImageUrl")
            .thumbnail(0.1f)
            .transform(CenterInside())
            .into(binding.movieImageDetails)
    }


    /**
     * To bind userRating to progressBar
     * */
    private fun bindUserRating(userRating: Float) {
        progressBarBinding.apply {
            val rounded = String.format("%.0f", userRating * 10f)
            val ratePercentage = "${rounded}%"
            progressText.text = ratePercentage
            progressBar.progress = (userRating * 10f).toInt()
            progressBar.max = 100
        }
    }


    /**
     * To bind recyclerView with movieDetails
     * */
    private fun bindRvWithMovieDetails(overview: String, userRating: String, releaseDate: String) {
        binding.apply {
            val contents = listOf(
                overview,
                userRating,
                releaseDate
            )
            adapter.contents = contents
            moviesDetailsRv.layoutManager = LinearLayoutManager(requireContext())
            moviesDetailsRv.adapter = adapter
        }
    }





    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}