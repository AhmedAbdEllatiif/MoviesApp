package com.ahmed.moviesapp.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ahmed.moviesapp.domain.FireBaseRepository
import com.ahmed.moviesapp.domain.RoomRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
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
                    allMovies.forEach {
                        Log.e(TAG, "doWork: upload ${it.movie.title}")
                        firebaseRepo.updateOrWriteNavMovie(it)
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