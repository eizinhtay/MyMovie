package com.example.mymovie.ui.movie

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mymovie.data.models.movie.Movie
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
                repository.addMovies(it.movies)
                Log.d("MoviesViewModel", "response::${repository.getMoviesFromDb()}")
            },
            onFailure = {

            }
        )
    }

    fun getLocalMovies(): List<Movie> {
        return repository.getMoviesFromDb()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}