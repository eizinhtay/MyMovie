package com.example.mymovie.data.repository.movieDetail

import com.example.mymovie.data.models.movieDetail.MovieDetails
import com.example.mymovie.di.MovieApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MovieDetailRepository @Inject constructor(
    private val movieApiService: MovieApiService,
) {

    /**
     * Get MovieDetail Api call
     */
    fun getMovieDetail(
        movieId:Int,
        compositeDisposable: CompositeDisposable,
        onFailure: (String) -> Unit,
        onSuccess: (MovieDetails) -> Unit
    ) {
        movieApiService.apiService?.getMovieDetails(movieId)
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