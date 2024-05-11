package com.example.mymovie.data.repository.movie

import com.example.mymovie.data.models.movie.MoviesResponse
import com.example.mymovie.di.MovieApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val movieApiService: MovieApiService) {

    /**
     * Get Movies Api call
     */
    fun getMovies(
        compositeDisposable: CompositeDisposable,
        onFailure: (String) -> Unit,
        onSuccess: (MoviesResponse) -> Unit
    ) {
        movieApiService.apiService?.getMovies()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                onSuccess(it)

            }, {
                onFailure(it.localizedMessage ?: "")
            })?.let {
                compositeDisposable.add(
                    it
                )
            }
    }

}