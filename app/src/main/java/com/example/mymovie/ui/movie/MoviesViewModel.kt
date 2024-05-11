package com.example.mymovie.ui.movie

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mymovie.data.repository.movie.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val repository: MoviesRepository) :
    ViewModel() {


    private var compositeDisposable = CompositeDisposable()


    fun getMovies(
    ) {
        repository.getMovies(
            compositeDisposable,
            onSuccess = {
                Log.d("MoviesViewModel", "response::$it")

            },
            onFailure = {

            }


        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}