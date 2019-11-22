package com.ctyeung.dictation.room

import androidx.lifecycle.LiveData

class VerseRepository(private val verseDao: VerseDao)
{

    val stanza:LiveData<List<VerseEntity>> = verseDao.getAllVerses()

    suspend fun insert(verse:VerseEntity)
    {
        verseDao.insert(verse)
    }
}