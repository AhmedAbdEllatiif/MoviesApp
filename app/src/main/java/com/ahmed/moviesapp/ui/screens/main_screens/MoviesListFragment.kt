package com.ahmed.moviesapp.ui.screens.main_screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

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



        observeUiState()

        observeMoviesPerPage()

        adapter.onItemClicked = this

        viewModel.errorData.observe(viewLifecycleOwner, {
            Log.e("MoviesListFragment", "onViewCreated: ${it.message}" )
        })

    }


    private fun observeUiState(){
        viewModel.uiUIState.observe(viewLifecycleOwner, {
            when(it){
                LoadingDataState.DataLoaded -> binding.loadingProgress.visibility = View.GONE
                is LoadingDataState.Failed -> showError(it.error)
                LoadingDataState.Loading -> binding.loadingProgress.visibility = View.VISIBLE
                LoadingDataState.None -> binding.loadingProgress.visibility = View.VISIBLE
            }
        })
    }

    /**
     * To observe moviesList per page then submit data to the adapter
     * */
    private fun observeMoviesPerPage(){
        viewModel.updateUiState(LoadingDataState.Loading)
        viewModel.moviesPerPage.observe(viewLifecycleOwner, { pagingMovie ->
            if (pagingMovie != null) {
                adapter.submitData(viewLifecycleOwner.lifecycle, pagingMovie)
                viewModel.updateUiState(LoadingDataState.DataLoaded)
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


    /**
     * To show the error message inside toast
     * */
    private fun showError(errorMessage: String) {
        Toast.makeText(
            requireContext(),
            errorMessage,
            Toast.LENGTH_LONG
        ).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}