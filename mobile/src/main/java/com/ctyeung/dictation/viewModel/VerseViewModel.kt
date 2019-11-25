package com.ctyeung.dictation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ctyeung.dictation.room.VerseDatabase
import com.ctyeung.dictation.room.Verse
import com.ctyeung.dictation.room.VerseRepository
import kotlinx.coroutines.launch

class VerseViewModel (application: Application) : AndroidViewModel(application)
{
    var repository:VerseRepository
    var stanza:LiveData<List<Verse>>

    init {
        val verseDao = VerseDatabase.getDatabase(application, viewModelScope).verseDao()
        repository = VerseRepository(verseDao)
        stanza = repository.stanza
    }

    fun insert(verse:Verse) = viewModelScope.launch {
        repository.insert(verse)
    }

    fun deleteSelected()= viewModelScope.launch {
        repository.deleteSelected()
    }

    fun clear() = viewModelScope.launch {
        repository.clear()
    }

    fun update(verse:Verse) = viewModelScope.launch {
        repository.update(verse)
    }
}

