package com.ctyeung.dictatekotlin.room

import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VerseRepository @Inject constructor(private val verseDao: VerseDao) {
    private val dbMapper = DbMapper()
    // var stanza:LiveData<List<Verse>> = verseDao.getAllVerses()

    val getAll = verseDao.getAllVerses()
        .map { dbMapper.mapVerses(it) }
        .catch {}
        .asLiveData()

    suspend fun insert(verse: Verse) {
        verseDao.insert(verse)
    }

    suspend fun clear() {
        verseDao.deleteAll()
    }

    suspend fun deleteSelected() {
        verseDao.deleteSelected()
    }

    suspend fun update(verse: Verse) {
        verseDao.update(verse)
    }
}