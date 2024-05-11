package com.example.mymovie.di

import com.example.mymovie.data.db.MoviesDao
import com.example.mymovie.data.repository.movie.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMovieRepository(apiService: MovieApiService,moviesDao: MoviesDao): MoviesRepository {
        return MoviesRepository(apiService,moviesDao)
    }
}
