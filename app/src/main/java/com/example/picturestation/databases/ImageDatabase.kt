package com.example.picturestation.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.picturestation.databases.entities.ImageDirectory

@Database(entities = [ImageDirectory::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ImageDatabase: RoomDatabase(){
    abstract  fun imageDirectoryDao(): ImageDirectoryDao
}