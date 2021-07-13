package com.ahmed.moviesapp.ui.screens.main_screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
            moviesRv.layoutManager  = LinearLayoutManager(context)
            moviesRv.adapter = adapter.withLoadStateHeaderAndFooter(
                header = MoviesLoadAdapter{adapter.retry() },
                footer = MoviesLoadAdapter{adapter.retry()},
            )

            viewModel.isUserCreated.observe(viewLifecycleOwner,{ isUserCreated ->
                if(isUserCreated){
                    Log.e(TAG, "onViewCreated: New User Created" )
                }else{
                    Log.e(TAG, "onViewCreated: Failed to create new User" )

                }

            })

            btn.setOnClickListener{
               viewModel.createNewUser("ahmedmohamedaneng@gmail.com","123456")
            }
        }



        observeMoviesPerPage()
    }

    private fun observeMoviesPerPage(){
        viewModel.moviesPerPage.observe(viewLifecycleOwner, { pagingMovie ->
            adapter.submitData(viewLifecycleOwner.lifecycle,pagingMovie)
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