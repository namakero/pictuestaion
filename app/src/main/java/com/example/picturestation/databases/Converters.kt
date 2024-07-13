package com.example.picturestation.databases

import android.net.Uri
import androidx.room.TypeConverter
import com.example.picturestation.databases.entities.ImageDirectory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromImageDirectory(imageDirectory: ImageDirectory?): String? {
        return gson.toJson(imageDirectory)
    }

    @TypeConverter
    fun toImageDirectory(imageDirectoryString: String?): ImageDirectory? {
        if (imageDirectoryString.isNullOrEmpty()) {
            return null
        }
        return gson.fromJson(imageDirectoryString, ImageDirectory::class.java)
    }

    @TypeConverter
    fun fromImageList(imageList: List<String>?): String? {
        return gson.toJson(imageList)
    }

//    @TypeConverter
//    fun toImageList(imageListString: String?): List<String>? {
//        val listType = object : TypeToken<List<String>>() {}.type
//        return gson.fromJson<List<String>>(imageListString, listType)
//    }

    @TypeConverter
    fun toImageMutableList(mutableImageListString: String?): MutableList<String> {
        val mutableListType = object : TypeToken<MutableList<String>>() {}.type
        return gson.fromJson<MutableList<String>>(mutableImageListString, mutableListType)
    }

    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }
}
