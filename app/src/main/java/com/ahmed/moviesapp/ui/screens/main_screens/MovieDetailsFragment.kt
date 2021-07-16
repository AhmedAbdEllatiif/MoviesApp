package com.ahmed.moviesapp.ui.screens.main_screens

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmed.moviesapp.R
import com.ahmed.moviesapp.databinding.FragmentDetailsMovieBinding
import com.ahmed.moviesapp.databinding.ProgressbarWithTextBinding
import com.ahmed.moviesapp.di.ApiModule
import com.ahmed.moviesapp.di.AppModule
import com.ahmed.moviesapp.ui.adapters.MovieDetailsAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.CenterInside
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_details_movie) {

    @Inject
    @Named(ApiModule.BASE_IMAGE_URL_W500)
    lateinit var baseImageUrl :String

    @Inject
    @Named(ApiModule.BASE_IMAGE_URL_W300)
    lateinit var smallBaseImageUrl :String

    @Inject
    lateinit var adapter: MovieDetailsAdapter

    // ViewModel
    private val viewModel: MainViewModel by activityViewModels()

    // binding
    private var _binding: FragmentDetailsMovieBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialize _binding
        _binding = FragmentDetailsMovieBinding.bind(view)
        Log.e(TAG, "onViewCreated:")

        val progressbarBinding: ProgressbarWithTextBinding = binding.progressBarContainer



        viewModel.movieItemLiveData.observe(viewLifecycleOwner, { movieItem ->
            Log.e(TAG, "onViewCreated:movieItemTitle: ${movieItem.original_title}")
            binding.apply {
                val contents = listOf(
                    movieItem.plot_synopsis,
                    movieItem.user_rating.toString(),
                    movieItem.release_date
                )
                adapter.contents = contents
                moviesDetailsRv.layoutManager = LinearLayoutManager(requireContext())
                moviesDetailsRv.adapter = adapter

                collapsingToolbar.title = movieItem.original_title
                // setup Glide request without the into() method
                // setup Glide request without the into() method

                val thumbnailRequest: RequestBuilder<Drawable> = Glide
                    .with(requireActivity())
                    .load("$smallBaseImageUrl${movieItem.movie_poster}")


                Glide.with(requireActivity())
                    .load("$baseImageUrl${movieItem.movie_poster}")
                    .thumbnail(thumbnailRequest)
                    .transform(CenterInside())
                    .into(movieImageDetails)
            }

            val rounded = String.format("%.0f", movieItem.user_rating * 10f)
            val ratePercentage = "${rounded}%"
            progressbarBinding.progressText.text = ratePercentage
            progressbarBinding.progressBar.progress = (movieItem.user_rating * 10f).toInt()
            progressbarBinding.progressBar.max = 100

        })


        /*viewModel.movieItemLiveData.observe(viewLifecycleOwner, { movieItem ->
            Log.e(TAG, "onViewCreated:movieItemTitle: ${movieItem.original_title}" )
            binding.apply {
                txtTitle.text = movieItem.original_title
                collapsingToolbar.title = movieItem.original_title
                txtOverview.text = movieItem.plot_synopsis
                txtRate.text = movieItem.user_rating.toString()
                txtDate.text = movieItem.release_date
                Glide.with(requireActivity())
                    .load("$baseImageUrl${movieItem.movie_poster}")
                    .thumbnail(0.1f)
                    .transform(CenterInside())
                    .into(movieImageDetails)
            }

            val rounded = String.format("%.0f", movieItem.user_rating * 10f)
            val ratePercentage = "${rounded}%"
            progressbarBinding.progressText.text = ratePercentage
            progressbarBinding.progressBar.progress = (movieItem.user_rating * 10f).toInt()
            progressbarBinding.progressBar.max = 100

        })*/




        /*val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true *//* enabled by default *//*) {
                override fun handleOnBackPressed() {
                    Log.e(TAG, "handleOnBackPressed: " )

                    //Log.e(TAG, "handleOnBackPressed: popBackStack >>${findNavController().popBackStack()} ", )
                    Log.e(TAG, "handleOnBackPressed: navigateUp >>${findNavController().navigateUp()} ", )
                    //findNavController().navigate(R.id.action_MovieDetailsFragment_to_moviesListFragment)
                    findNavController().navigateUp()

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)*/

        /* Glide.with(this)
             .load("http://image.tmdb.org/t/p/w500/dq18nCTTLpy9PmtzZI6Y2yAgdw5.jpg")

             .into(binding.movieImageDetails)*/
/* .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                     startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

            })*/

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "SingleMovieFragment"
    }
}