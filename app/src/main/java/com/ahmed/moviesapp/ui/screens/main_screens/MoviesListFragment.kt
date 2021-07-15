package com.ahmed.moviesapp.ui.screens.main_screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import com.ahmed.moviesapp.R
import com.ahmed.moviesapp.data.MovieItem
import com.ahmed.moviesapp.databinding.FragmentMoviesListBinding

import com.ahmed.moviesapp.ui.adapters.MoviesAdapter
import com.ahmed.moviesapp.ui.adapters.MoviesLoadAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MoviesListFragment : Fragment(R.layout.fragment_movies_list) , MoviesAdapter.OnItemClicked {

    @Inject
    lateinit var adapter :MoviesAdapter

    // ViewModel
    private val viewModel: MainViewModel by activityViewModels()

    // binding
    private var _binding : FragmentMoviesListBinding? = null
    private val binding get() = _binding!!



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialize _binding
        _binding = FragmentMoviesListBinding.bind(view)

        // Bind with recyclerView
        binding.apply {
            moviesRv.setHasFixedSize(true)
            moviesRv.layoutManager  = GridLayoutManager(requireActivity(), 2)
            moviesRv.adapter = adapter.withLoadStateHeaderAndFooter(
                header = MoviesLoadAdapter { adapter.retry() },
                footer = MoviesLoadAdapter { adapter.retry() },
            )

        }

        observeMoviesPerPage()

        observerMovieItemLiveData()

        adapter.onItemClicked = this

    }



    /**
     * To observe moviesList per page then submit data to the adapter
     * */
    private fun observeMoviesPerPage(){
        viewModel.moviesPerPage.observe(viewLifecycleOwner, { pagingMovie ->
            adapter.submitData(viewLifecycleOwner.lifecycle, pagingMovie)
        })
    }

    /**
     * To observe the movie item clicked by user
     * */
    private fun observerMovieItemLiveData(){
        adapter.movieItemLiveData.observe(viewLifecycleOwner, { movieItem ->
            if (movieItem != null) {
                //viewModel.movieItemLiveData.value = movieItem
                //viewModel.movieItemLiveData.postValue(movieItem)
                //viewModel.updateOrWriteNavMovie(movieItem)
            }
        })

    }

    /**
     * To navigate to MovieDetailsFragment
     * */
    private fun navigateToMovieDetailsFragment(){
        findNavController().navigate(R.id.action_moviesListFragment_to_movieSingleFragment)
    }


    /**
     * Impl of onClick item in RecyclerView
     * */
    override fun onClick(movieItem: MovieItem) {
        viewModel.movieItemLiveData.value = movieItem
        //viewModel.movieItemLiveData.postValue(movieItem)
        navigateToMovieDetailsFragment()
        viewModel.updateOrWriteNavMovie(movieItem)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}