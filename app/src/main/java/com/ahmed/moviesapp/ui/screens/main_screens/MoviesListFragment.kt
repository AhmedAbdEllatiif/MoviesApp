package com.ahmed.moviesapp.ui.screens.main_screens

import android.content.Intent
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
import com.ahmed.moviesapp.ui.screens.start_app_screens.StartActivityActivity
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

        // implementation of MoviesAdapter.OnItemClicked
        adapter.onItemClicked = this

        // To observe changes of ui
        observeUiState()

        // bind movies list with recyclerView
        bindMovieListWithRV()

        // To observe moviesList per page then submit data to the adapter
        observeMoviesPerPage()

        // To observe bad internet connection
        observeBadInternetConnection()

        //  To logOut
        submitLogout()

    }



    /**
     * To observe changes of ui
     * */
    private fun observeUiState(){
        viewModel.uiUIState.observe(viewLifecycleOwner, {
            when(it){
                MainScreenUIState.DataLoaded -> binding.loadingProgress.visibility = View.GONE
                is MainScreenUIState.Failed -> showError(it.error)
                MainScreenUIState.Loading -> binding.loadingProgress.visibility = View.VISIBLE
                MainScreenUIState.None -> binding.loadingProgress.visibility = View.VISIBLE
                MainScreenUIState.LoggedOut -> navigateToStartActivity()
            }
        })
    }

    /**
     * To observe moviesList per page then submit data to the adapter
     * */
    private fun observeMoviesPerPage(){
        viewModel.updateUiState(MainScreenUIState.Loading)
        viewModel.moviesPerPage.observe(viewLifecycleOwner, { pagingMovie ->
            if (pagingMovie != null) {
                adapter.submitData(viewLifecycleOwner.lifecycle, pagingMovie)
                viewModel.updateUiState(MainScreenUIState.DataLoaded)
            }
        })
    }

    /**
     * To observe bad internet connection
     * */
    private fun observeBadInternetConnection(){
        viewModel.errorData.observe(viewLifecycleOwner, {
            Log.e("MoviesListFragment", "onViewCreated: ${it.message}" )
            showError("Bad Internet connection")
        })
    }

    /**
     * bind movies list with recyclerView
     * */
    private fun bindMovieListWithRV(){
        binding.apply {
            moviesRv.setHasFixedSize(true)
            moviesRv.layoutManager  = GridLayoutManager(requireActivity(), 2)
            moviesRv.adapter = adapter.withLoadStateHeaderAndFooter(
                header = MoviesLoadAdapter { adapter.retry() },
                footer = MoviesLoadAdapter { adapter.retry() },
            )
        }
    }


    /**
     * To navigate to MovieDetailsFragment
     * */
    private fun navigateToMovieDetailsFragment(){
        findNavController().navigate(R.id.action_moviesListFragment_to_movieDetailsFragment)
    }

    /**
     * To navigate to loginScreen after user logOut
     * */
    private fun navigateToStartActivity(){
        val intent = Intent(requireActivity(),StartActivityActivity::class.java)
        intent.putExtra("isLoggedOut",true)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }

    /**
     * To logOut
     * */
    private fun submitLogout(){
        binding.imgLogout.setOnClickListener {
            viewModel.logOut()
        }
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