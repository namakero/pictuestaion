package com.example.picturestation.databases

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.picturestation.databases.entities.ImageDirectory
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDirectoryDao{
    @Query("SELECT * FROM imagedirectory")
    fun getAll(): Flow<List<ImageDirectory>>

    @Query("SELECT * FROM imagedirectory WHERE id == :id")
    fun get(id: Int): Flow<ImageDirectory>

    @Insert
    suspend fun insert(directoryImageEntities: ImageDirectory)

    @Delete
    suspend fun delete(directoryImageEntity: ImageDirectory)

    @Update
    suspend fun update(directoryImageEntity: ImageDirectory)

    @Query("DELETE FROM imagedirectory")
    suspend fun deleteAll()
}