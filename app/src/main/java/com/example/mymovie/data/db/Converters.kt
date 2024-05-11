package com.example.mymovie.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

 class Converters {
    @TypeConverter
    fun fromItemList(itemList: List<Int>): String {
        return Gson().toJson(itemList)
    }

    @TypeConverter
    fun toItemList(itemListString: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(itemListString, type)
    }
}
