package com.example.mymovie.data.repository.movie

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.mymovie.data.db.MoviesDao
import com.example.mymovie.data.models.movie.Movie
import com.example.mymovie.di.MovieApiService
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource(
    private val movieApiService: MovieApiService,
    private val moviesDao: MoviesDao,
    private val query: String,

    ) : RxPagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        // Get the key for refreshing the data
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    }

    override val keyReuseSupported = true

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Movie>> {
        val pageNumber: Int = params.key ?: 1
        val previousPageNumber: Int? = null
        var resultItemList: MutableList<Movie>?

        var nextPageNumber: Int? = params.key
        if (nextPageNumber == null) {
            nextPageNumber = 1
        }


        return movieApiService.apiService?.getMoviess(nextPageNumber,query)
            // Single-based network load
            ?.subscribeOn(Schedulers.io())
            ?.map { result ->
                try {
                    nextPageNumber =
                        if (result.movies.isNotEmpty()) pageNumber + 1 else null
                    if (result.movies.isNotEmpty()) {
                        resultItemList = result.movies.toMutableList()
                        moviesDao.clearAll()
                        moviesDao.addMovies(result.movies)


                        return@map LoadResult.Page(
                            data = resultItemList ?: mutableListOf(),
                            prevKey = previousPageNumber,
                            nextKey = nextPageNumber
                        )
                    } else {
                        if ((nextPageNumber ?: 0) == 1){
                            LoadResult.Error(Exception("NoData"))

                        }else{
                            LoadResult.Error(Exception("NoNextData"))

                        }
                    }
                } catch (_: Exception) {
                    LoadResult.Error(Exception("Null data"))
                }

            }
            ?.onErrorReturn { e ->
                when (e) {
                    // Retrofit calls that return the body type throw either IOException for
                    // network failures, or HttpException for any non-2xx HTTP status codes.
                    // This code reports all errors to the UI, but you can inspect/wrap the
                    // exceptions to provide more context.
                    is IOException -> LoadResult.Error(e)
                    is HttpException -> LoadResult.Error(e)
                    else -> throw e
                }
            } ?: Single.error(Throwable("error"))

    }


}
