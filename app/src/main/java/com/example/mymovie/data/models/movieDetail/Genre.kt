package com.example.mymovie.data.models.movieDetail

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("name")
    val name: String
)