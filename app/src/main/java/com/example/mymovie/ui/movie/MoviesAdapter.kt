package com.example.mymovie.ui.movie

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymovie.data.models.movie.Movie
import com.example.mymovie.databinding.ViewHolderMovieBinding
import com.example.mymovie.utils.IMAGE_BASE_URL


class MoviesAdapter() :
    RecyclerView.Adapter<MovieViewHolder>() {

    private var itemList = mutableListOf<Movie>()
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        mContext = parent.context
        val mBinding = ViewHolderMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(mBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        if (itemList.isNotEmpty()) {
            holder.bindData(itemList[position])
        }
    }

    fun setItems(items: MutableList<Movie>?) {

        if (items != null) {
            itemList = items
            notifyItemRangeInserted(0, itemList.size)
        }
    }

    fun clearList() {
        itemList.clear()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}


class MovieViewHolder(
    private val binding: ViewHolderMovieBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private var mMovieVO: Movie? = null

    init {

        binding.root.setOnClickListener {

        }
    }

    fun bindData(movieVO: Movie) {

        mMovieVO = movieVO
        //binding.helpCenterObj = itemVO
        binding.apply {
            Glide.with(itemView.context).load("$IMAGE_BASE_URL${mMovieVO!!.posterPath}")
                .into(ivMovieImage)
            tvMovieName.text = movieVO.title
            tvMovieRating.text = movieVO.voteAverage?.toString()
            rbMovieRating.rating = movieVO.voteAverage?.div(2)?.toFloat() ?: 0.0f
        }
    }
}