package com.example.mymovie.data.remote

import dagger.Module


@Module
class MovieApiService {
    val apiService: MovieApiInterface? = MovieAPIClient.getClient()?.create(MovieApiInterface::class.java)
}
