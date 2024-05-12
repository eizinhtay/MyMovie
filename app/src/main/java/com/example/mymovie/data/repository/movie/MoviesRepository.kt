package com.example.mymovie.data.repository.movie

import com.example.mymovie.data.db.MoviesDao
import com.example.mymovie.data.models.movie.Movie
import com.example.mymovie.data.models.movie.MoviesResponse
import com.example.mymovie.di.MovieApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val movieApiService: MovieApiService,
    private val moviesDao: MoviesDao
) {

    /**
     * Get Movies Api call
     */
    fun getMovies(
        page:Int,
        compositeDisposable: CompositeDisposable,
        onFailure: (String) -> Unit,
        onSuccess: (MoviesResponse) -> Unit
    ) {
        movieApiService.apiService?.getMovies(page)
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

    fun getMoviesFromDb(): List<Movie> {
        return moviesDao.getMovies()
    }

    fun addMovies(movies:List<Movie>){
        moviesDao.addMovies(movies = movies)
    }

}