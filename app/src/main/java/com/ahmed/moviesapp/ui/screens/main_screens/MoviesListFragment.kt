package com.ahmed.moviesapp.ui.screens.main_screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmed.moviesapp.R
import com.ahmed.moviesapp.databinding.FragmentMoviesListBinding
import com.ahmed.moviesapp.ui.adapters.MoviesAdapter
import com.ahmed.moviesapp.ui.adapters.MoviesLoadAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MoviesListFragment : Fragment(R.layout.fragment_movies_list) {

    // ViewModel
    private val viewModel: MainViewModel by viewModels()

    // binding
    private var _binding : FragmentMoviesListBinding? = null
    private val binding get() = _binding!!

    // initialize MoviesAdapter
    private val adapter = MoviesAdapter()

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

        observerClickedMovieItem()
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
    private fun observerClickedMovieItem(){
        adapter.movieItemLiveData.observe(viewLifecycleOwner, { movieItem ->
            if (movieItem != null) {
                viewModel.updateOrWriteNavMovie(movieItem)
            }
        })

    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
     private const val TAG = "MoviesListFragment"
    }
}