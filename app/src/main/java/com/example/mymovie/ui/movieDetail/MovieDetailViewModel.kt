package com.example.mymovie.ui.movieDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymovie.data.models.movieDetail.MovieDetails
import com.example.mymovie.data.repository.movieDetail.MovieDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val repository: MovieDetailRepository) :
    ViewModel() {

    private var compositeDisposable = CompositeDisposable()

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    private val _movieError = MutableLiveData<String>()
    val movieError: LiveData<String> = _movieError


    fun getMovieDetail(
        movieId: Int,

        ) {
        repository.getMovieDetail(
            movieId,
            compositeDisposable,
            onSuccess = {
                _movieDetails.value = it

            },
            onFailure = {
                _movieError.value = it
            }
        )
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}