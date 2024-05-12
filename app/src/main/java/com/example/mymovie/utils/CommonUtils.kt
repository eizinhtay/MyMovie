package com.example.mymovie.utils

import android.content.Context
import android.widget.Toast

fun showToastMessage(context: Context, message: String?) {
    Toast.makeText(context, message ?: "", Toast.LENGTH_SHORT).show()
}