package com.example.forecastapp.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {

    @TypeConverter
    public fun fromString(value: String): List<String> {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    public fun fromArrayList(list: List<String>): String{
        val gson = Gson()
        val json = gson.toJson(list)
        return json
    }
}