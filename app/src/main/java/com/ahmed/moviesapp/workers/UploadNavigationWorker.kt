package com.ahmed.moviesapp.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ahmed.moviesapp.data.firebaseData.NavMovie
import com.ahmed.moviesapp.domain.FireBaseRepository
import com.ahmed.moviesapp.domain.RoomRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltWorker
class UploadNavigationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    var firebaseRepo: FireBaseRepository,
    var roomRepository: RoomRepository,

) : Worker(
    context,
    workerParams
) {

    override fun doWork(): Result {
        Log.e(TAG, "doWork: Start")
        runBlocking {
            launch {
                roomRepository.getAllMovies().collect { allMovies ->
                    allMovies.forEach { navMovie ->
                        Log.e(TAG, "doWork: upload ${navMovie.movie.title}")
                        firebaseRepo.updateOrWriteNavMovie(navMovie)

                    }
                }
            }
        }

        return Result.success()
    }



    companion object {
        private const val TAG = "UploadNavigationWorker"
    }

}