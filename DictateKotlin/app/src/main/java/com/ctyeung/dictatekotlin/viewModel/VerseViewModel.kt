package com.ctyeung.dictatekotlin.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctyeung.dictatekotlin.R
import com.ctyeung.dictatekotlin.room.Verse
import com.ctyeung.dictatekotlin.room.VerseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerseViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val db: VerseRepository):ViewModel()
{
    var stanza:LiveData<List<Verse>> = db.getAll
    var txtInfo:String = "0"

    /*
     * Display appropriate text information
     */
    fun updateInfo()
    {
        val size = (stanza.value?.size ?: 0)
        txtInfo = if( size > 0) {
            context.resources.getString(R.string.info_count) + " " + size
        } else {
            context.resources.getString(R.string.info)
        }
    }

    fun insert(verse:Verse) = viewModelScope.launch(Dispatchers.IO) {
        db.insert(verse)
    }

    fun deleteSelected() = viewModelScope.launch(Dispatchers.IO) {
        db.deleteSelected()
    }

    fun clear() = viewModelScope.launch(Dispatchers.IO) {
        db.clear()
    }

    fun update(verse:Verse) = viewModelScope.launch(Dispatchers.IO) {
        db.update(verse)
    }

    fun serialize():String {
        // something to save
        val builder: StringBuilder = StringBuilder()

        // loop through list and write to file
        val stanza = stanza.value?:emptyList<Verse>()
        for (v in stanza) {
            builder.append(v.verse+ "\r\n")
        }

        return builder.toString()
    }

    private fun verseCount():Int {
        return stanza.value?.size?:0
    }

    fun allSelected():Boolean {
        val count = selectedCount()
        if(0==count || count >=verseCount())
            return true

        return false
    }

    private fun selectedCount():Int {
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

