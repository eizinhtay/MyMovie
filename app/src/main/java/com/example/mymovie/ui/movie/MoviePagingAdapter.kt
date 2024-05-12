package com.example.mymovie.ui.movie

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymovie.data.models.movie.Movie
import com.example.mymovie.databinding.ViewHolderMovieBinding
import com.example.mymovie.utils.IMAGE_BASE_URL
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MovieRecyclerAdapter
@Inject
constructor(
    @ApplicationContext val context: Context,
    private val moviesDelegate: MoviesDelegate,
    ): PagingDataAdapter<Movie, MovieRecyclerAdapter.MovieViewHolder>(
    MovieDiffCallback
) {

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ViewHolderMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),moviesDelegate
        )
    }

    inner class MovieViewHolder(
        private val binding: ViewHolderMovieBinding,
        private val moviesDelegate: MoviesDelegate,

        ): RecyclerView.ViewHolder(binding.root) {
        private var mMovieVO: Movie? = null

        fun bind(movieVO: Movie?) {
            mMovieVO = movieVO
            binding.apply {
                Glide.with(itemView.context).load("$IMAGE_BASE_URL${mMovieVO?.posterPath}")
                    .into(ivMovieImage)
                tvMovieName.text = movieVO?.title
                tvMovieRating.text = movieVO?.voteAverage?.toString()
                rbMovieRating.rating = movieVO?.voteAverage?.div(2)?.toFloat() ?: 0.0f

                root.setOnClickListener {
                    mMovieVO?.id?.let { id -> moviesDelegate.onMovieItemClick(id) }

                }
            }

        }


    }
}