package com.ctyeung.dictation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ctyeung.dictation.room.VerseDatabase
import com.ctyeung.dictation.room.VerseEntity
import com.ctyeung.dictation.room.VerseRepository
import kotlinx.coroutines.launch

class VerseViewModel (application: Application) : AndroidViewModel(application)
{
    lateinit var repository:VerseRepository
    lateinit var stanza:LiveData<List<VerseEntity>>

    init {
        val verseDao = VerseDatabase.getDatabase(application).verseDao()
        repository = VerseRepository(verseDao)
        stanza = repository.stanza
    }

    fun insert(verse:VerseEntity) = viewModelScope.launch {
        repository.insert(verse)
    }
}

