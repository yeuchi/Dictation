package com.ctyeung.dictation.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/*
 * References
 * https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#2
 */
@Database(entities = arrayOf(VerseEntity::class), version = 1, exportSchema = false)
public abstract class VerseDatabase : RoomDatabase ()
{
    abstract fun verseDao():VerseDao

    companion object {
        @Volatile
        private var INSTANCE: VerseDatabase? = null

        fun getDatabase(context:Context):VerseDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    VerseDatabase::class.java,
                    "verse_database").build()

                INSTANCE = instance
                return instance
            }
        }
    }
}