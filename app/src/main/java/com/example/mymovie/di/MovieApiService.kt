package com.example.mymovie.di

import com.example.mymovie.data.remote.MovieAPIClient
import com.example.mymovie.data.remote.MovieApiInterface
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class MovieApiService {
    val apiService: MovieApiInterface? = MovieAPIClient.getClient()?.create(MovieApiInterface::class.java)
}
