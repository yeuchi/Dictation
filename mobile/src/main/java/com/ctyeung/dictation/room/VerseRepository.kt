package com.ctyeung.dictation.room

import androidx.lifecycle.LiveData

class VerseRepository(private val verseDao: VerseDao)
{
    var stanza:LiveData<List<Verse>> = verseDao.getAllVerses()

    suspend fun insert(verse:Verse)
    {
        verseDao.insert(verse)
    }

    suspend fun clear()
    {
        verseDao.deleteAll()
    }

    suspend fun deleteSelected()
    {
        verseDao.deleteSelected()
    }

    suspend fun update(verse:Verse)
    {
        verseDao.update(verse)
    }

    fun getCountSelected():Int
    {
        //val list = verseDao.getSelected()
        //return list?.value?.size?:0

        var count = 0
        val list = stanza.value
        for(i in list!!.indices) {
            val verse = list[i]
            if(verse.isSelected)
                count ++
        }
        return count
    }
}