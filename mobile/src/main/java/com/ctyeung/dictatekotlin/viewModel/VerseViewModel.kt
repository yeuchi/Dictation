package com.ctyeung.dictatekotlin.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ctyeung.dictatekotlin.R
import com.ctyeung.dictatekotlin.room.VerseDatabase
import com.ctyeung.dictatekotlin.room.Verse
import com.ctyeung.dictatekotlin.room.VerseRepository
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class VerseViewModel (application: Application) : AndroidViewModel(application)
{
    private val context = getApplication<Application>().applicationContext
    var repository:VerseRepository
    var stanza:LiveData<List<Verse>>
    var txtInfo:String = "0"

    init {
        val verseDao = VerseDatabase.getDatabase(application, viewModelScope).verseDao()
        repository = VerseRepository(verseDao)
        stanza = repository.stanza
    }


    /*
     * Display appropriate text information
     */
    fun updateInfo(count:Int=0)
    {
        var str:String
        if(count > 0)
        {
            txtInfo = context.resources.getString(R.string.info_count) + " " + count
        }
        else {
            txtInfo = context.resources.getString(R.string.info)
        }
    }

    fun insert(verse:Verse) = viewModelScope.launch {
        repository.insert(verse)
    }

    fun deleteSelected() = viewModelScope.launch {
        repository.deleteSelected()
    }

    fun clear() = viewModelScope.launch {
        repository.clear()
    }

    fun update(verse:Verse) = viewModelScope.launch {
        repository.update(verse)
    }

    fun serialize2File(context:Context):Pair<Boolean, String> {
        // something to save
        var builder: StringBuilder = StringBuilder()

        // loop through list and write to file
        val stanza = stanza.value?:emptyList<Verse>()
        for (v in stanza) {
            builder.append(v.verse+ "\r\n")
        }

        val result = FileSerializer.write2file(context, builder.toString())
        return result
    }

    fun verseCount():Int {
        return stanza.value?.size?:0
    }

    fun selectedCount():Int {
        return repository.getCountSelected()
    }

    fun allSelected():Boolean {
        val count = selectedCount()
        if(0==count || count >=verseCount())
            return true

        return false
    }
}

