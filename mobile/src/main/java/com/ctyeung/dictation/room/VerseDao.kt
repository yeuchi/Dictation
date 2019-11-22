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
    @Query("SELECT * from stanza_table ORDER BY datetime")
    fun getAllVerses() : LiveData<List<VerseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(verse:VerseEntity)

    @Query("DELETE FROM stanza_table")
    suspend fun deleteAll()

    @Query("DELETE FROM stanza_table WHERE datetime < :seconds")
    suspend fun deleteEarlier(seconds:Long)

    @Query("DELETE FROM stanza_table WHERE datetime = :seconds")
    suspend fun deleteOn(seconds:Long)
}