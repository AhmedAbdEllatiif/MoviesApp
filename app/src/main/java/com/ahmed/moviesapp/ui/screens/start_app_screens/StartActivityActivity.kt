package com.ahmed.moviesapp.ui.screens.start_app_screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ahmed.moviesapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}