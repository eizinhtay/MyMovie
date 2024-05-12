package com.example.mymovie.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymovie.data.models.movie.Movie

@Dao
interface MoviesDao {

    /**
     * Get all movies from the movies table.
     *
     * @return all movies.
     */
    @Query("SELECT * FROM movies ORDER BY id")
    fun getMovies(): List<Movie>


    /**
     * Insert all movies.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovies(movies: List<Movie>)


    @Query("SELECT * FROM movies ORDER BY id")
    fun movies(): PagingSource<Int, Movie>

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%'")
    fun searchLocalMovies(query: String): PagingSource<Int, Movie>


    @Query("DELETE FROM movies")
     fun clearAll()

}