package com.ahmed.moviesapp.ui.screens.main_screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.ahmed.moviesapp.R

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

   public override fun onStart() {
        super.onStart()
       if(viewModel.isUserLoggedIn){
           Log.d(TAG, "onStart: User is not null" )
       }else{
           Log.d(TAG, "onStart: User is >>null<< " )
       }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}