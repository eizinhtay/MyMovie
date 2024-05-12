package com.example.mymovie.ui.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _movieList = MutableLiveData<MutableList<Movie>>()
    val movieList: LiveData<MutableList<Movie>> = _movieList

    fun getMovies(
    ) {
        repository.getMovies(
            page = 1,
            compositeDisposable=compositeDisposable,
            onSuccess = {
                _movieList.value = it.movies
                repository.addMovies(it.movies)
                Log.d("MoviesViewModel", "size::${it.movies.size}")

                Log.d("MoviesViewModel", "response::${repository.getMoviesFromDb().size}")
            },
            onFailure = {

            }
        )
    }

    fun getLocalMovies(): List<Movie> {
        return repository.getMoviesFromDb()
    }

    fun filterLocalMovies(query: String?): MutableList<Movie> {
        val filteredList = mutableListOf<Movie>()
        getLocalMovies().forEach {
            if (it.title?.contains(query.orEmpty(), ignoreCase = true) == true) {
                filteredList.add(it)
            }
        }
        return filteredList
    }

    fun filterMovies(query: String?): MutableList<Movie> {
        val filteredList = mutableListOf<Movie>()
        _movieList.value?.forEach {
            if (it.title?.contains(query.orEmpty(), ignoreCase = true) == true) {
                filteredList.add(it)
            }
        }
        return filteredList
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}