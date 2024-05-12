package com.example.mymovie.ui.movieVideo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymovie.data.models.movieVideo.MovieVideoResponse
import com.example.mymovie.data.repository.movieDetail.MovieDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class MovieVideoViewModel @Inject constructor(private val repository: MovieDetailRepository) :
    ViewModel() {

    private var compositeDisposable = CompositeDisposable()

    private val _movieVideoError = MutableLiveData<String>()
    val movieVideoError: LiveData<String> = _movieVideoError


    private val _movieVideos = MutableLiveData<MovieVideoResponse>()
    val movieVideos: LiveData<MovieVideoResponse> = _movieVideos


    fun getMovieVideos(
        movieId: Int,

        ) {
        repository.getMovieVideos(
            movieId,
            compositeDisposable,
            onSuccess = {
                _movieVideos.value = it

            },
            onFailure = {
                _movieVideoError.value = it
            }
        )
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}