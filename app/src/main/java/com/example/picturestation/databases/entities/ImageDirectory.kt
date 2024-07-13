package com.example.picturestation.databases.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ImageDirectory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val uri: String,
    var imageList: List<String> = listOf()
)