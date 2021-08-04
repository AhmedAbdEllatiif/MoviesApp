package com.ahmed.moviesapp.ui.screens.main_screens


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.ahmed.moviesapp.R
import com.ahmed.moviesapp.workers.UploadNavigationWorker

import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar.SECOND
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }

}