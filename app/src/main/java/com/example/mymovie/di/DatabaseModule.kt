package com.example.mymovie.di

import android.content.Context
import androidx.room.Room
import com.example.mymovie.data.db.MovieDatabase
import com.example.mymovie.data.db.MoviesDao
import com.example.mymovie.data.models.movie.Movie
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(context, MovieDatabase::class.java, "movies.db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMovieDao(movieDatabase: MovieDatabase): MoviesDao {
        return movieDatabase.movieDao()
    }

    @Provides
    fun provideEntity() = Movie()

}