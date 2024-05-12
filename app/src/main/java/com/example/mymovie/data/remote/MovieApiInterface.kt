package com.example.mymovie.data.remote

import com.example.mymovie.data.models.movie.MoviesResponse
import com.example.mymovie.data.models.movieDetail.MovieDetails
import com.example.mymovie.data.models.movieVideo.MovieVideoResponse
import com.example.mymovie.utils.GET_MOVIES
import com.example.mymovie.utils.GET_MOVIE_DETAIL
import com.example.mymovie.utils.GET_MOVIE_SEARCH
import com.example.mymovie.utils.GET_MOVIE_VIDEOS

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiInterface {

    @GET(GET_MOVIES)
    fun getMovies(@Query("page") page: Int): Observable<MoviesResponse>

    @GET(GET_MOVIE_DETAIL)
    fun getMovieDetails(@Path("movie_id") movieId: Int): Observable<MovieDetails>

    @GET(GET_MOVIE_SEARCH)
    fun getMovieSearch(@Query("query") query: String): Observable<MoviesResponse>

    @GET(GET_MOVIE_VIDEOS)
    fun getMovieVideos(@Path("movie_id") movieId: Int): Observable<MovieVideoResponse>


}