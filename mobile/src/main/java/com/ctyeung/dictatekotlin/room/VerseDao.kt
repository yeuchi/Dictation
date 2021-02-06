package com.ctyeung.dictatekotlin.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

/*
 * SQL statements here for CRUD operations
 */

@Dao
interface VerseDao
{
    @Query("SELECT * from verse_table ORDER BY datetime")
    fun getAllVerses() : Flow<List<Verse>>
   // fun getAllVerses() : LiveData<List<Verse>>

    @Query("SELECT * from verse_table WHERE isSelected = 1")
    fun getSelected() : Flow<List<Verse>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(verse:Verse)

    @Query("DELETE FROM verse_table")
    suspend fun deleteAll()

    /*
    @Query("DELETE FROM verse_table WHERE datetime < :seconds")
    suspend fun deleteEarlier(seconds:Long)

    @Query("DELETE FROM verse_table WHERE datetime = :seconds")
    suspend fun deleteOn(seconds:Long)

    @Query("SELECT * from verse_table WHERE datetime is :seconds")
    fun getVerse(seconds: Long) : List<Verse>
    */

    @Query("DELETE FROM verse_table WHERE isSelected = 1")
    suspend fun deleteSelected()

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(verse:Verse)
}