package com.ctyeung.dictation.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/*
 * SQL statements here for CRUD operations
 */

@Dao
interface VerseDao
{
    @Query("SELECT * from verse_table ORDER BY datetime")
    fun getAllVerses() : LiveData<List<Verse>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(verse:Verse)

    @Query("DELETE FROM verse_table")
    suspend fun deleteAll()

    @Query("DELETE FROM verse_table WHERE datetime < :seconds")
    suspend fun deleteEarlier(seconds:Long)

    @Query("DELETE FROM verse_table WHERE datetime = :seconds")
    suspend fun deleteOn(seconds:Long)
}