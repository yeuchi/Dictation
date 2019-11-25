package com.ctyeung.dictation.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * A stanza is a collection of verses
 */

@Entity(tableName = "verse_table")
class Verse(@PrimaryKey @ColumnInfo(name = "datetime") val datetime: Long,
            val verse:String,
            var isSelected:Boolean=false)
{
}