package com.ahmed.moviesapp.ui.screens.main_screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment

import android.view.View
import androidx.fragment.app.activityViewModels

import androidx.fragment.app.viewModels
import com.ahmed.moviesapp.R

import com.ahmed.moviesapp.databinding.FragmentDetailsMovieBinding
import com.ahmed.moviesapp.di.AppModule
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_details_movie) {

    @Inject
    @Named(AppModule.BASE_IMAGE_URL_W500)
    lateinit var baseImageUrl :String

    // ViewModel
    private val viewModel: MainViewModel by activityViewModels()

    // binding
    private var _binding: FragmentDetailsMovieBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialize _binding
        _binding = FragmentDetailsMovieBinding.bind(view)
        Log.e(TAG, "onViewCreated:" )





        viewModel.movieItemLiveData.observe(viewLifecycleOwner, { movieItem ->
            Log.e(TAG, "onViewCreated:movieItemTitle: ${movieItem.original_title}" )
            binding.apply {
                txtTitle.text = movieItem.original_title
                txtOverview.text = movieItem.plot_synopsis
                txtRate.text = movieItem.user_rating.toString()
                txtDate.text = movieItem.release_date
                Glide.with(requireActivity())
                    .load("$baseImageUrl${movieItem.movie_poster}")
                    .thumbnail(0.1f)
                    .transform(CenterInside())
                    .into(movieImageDetails)
            }
        })




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