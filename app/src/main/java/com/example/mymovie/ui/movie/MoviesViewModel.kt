package com.example.mymovie.ui.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mymovie.data.models.movie.Movie
import com.example.mymovie.data.repository.movie.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val repository: MoviesRepository) :
    ViewModel() {

    private var compositeDisposable = CompositeDisposable()

    private val _movieList = MutableLiveData<MutableList<Movie>>()
    val movieList: LiveData<MutableList<Movie>> = _movieList

    private val _moviePagingList = MutableLiveData<
            PagingData<Movie>>()
    val moviePagingList: LiveData<
            PagingData<Movie>> = _moviePagingList

    private val _movieLocalPagingList = MutableLiveData<
            PagingData<Movie>>()
    val movieLocalPagingList: LiveData<
            PagingData<Movie>> = _movieLocalPagingList


    fun getMovies(
    ) {
        repository.getMovies(
            page = 1,
            compositeDisposable=compositeDisposable,
            onSuccess = {
                _movieList.value = it.movies
                repository.addMovies(it.movies)
                Log.d("MoviesViewModel", "size::${it.movies.size}")

                Log.d("MoviesViewModel", "response::${repository.getMoviesFromDb().size}")
            },
            onFailure = {

            }
        )
    }

    fun getLocalMovies(): List<Movie> {
        return repository.getMoviesFromDb()
    }

    /*fun filterLocalMovies(query: String?): MutableList<Movie> {
        val filteredList = mutableListOf<Movie>()
        getLocalMovies().forEach {
            if (it.title?.contains(query.orEmpty(), ignoreCase = true) == true) {
                filteredList.add(it)
            }
        }
        return filteredList
    }*/

    /*fun filterMovies(query: String?): MutableList<Movie> {
        val filteredList = mutableListOf<Movie>()
        _movieList.value?.forEach {
            if (it.title?.contains(query.orEmpty(), ignoreCase = true) == true) {
                filteredList.add(it)
            }
        }


        return filteredList
    }*/

    fun getMoviesLocalPaging(){
        viewModelScope.launch {
            repository
                .getLocalMovies()
                .cachedIn(viewModelScope)
                .collect {
                    _movieLocalPagingList.value = it

                }

        }
    }

    fun searchMoviesLocalPaging(query: String){
        viewModelScope.launch {
            repository
                .searchLocalMovies(query)
                .cachedIn(viewModelScope)
                .collect {
                    _movieLocalPagingList.value = it

                }

        }
    }


    fun getAllMovies(query: String) {
        viewModelScope.launch {
            repository
                .getAllMovies(query=query)
                .cachedIn(viewModelScope)
                .collect {
                    _moviePagingList.value = it
                }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            repository
                .searchMovies(query=query)
                .cachedIn(viewModelScope)
                .collect {
                    _moviePagingList.value = it
                }
        }
    }
    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}